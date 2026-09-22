/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.pairip.native

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.util.inputStreamFromBundledResource
import kotlinx.serialization.json.Json
import net.fornwall.jelf.ElfFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.Base64
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream

private const val apkLibsPath = "lib/arm64-v8a/"

internal fun getNativeLibsPatch(app: String) = rawResourcePatch {

    execute {
        // replace pairipcore with stub
        val pairipLib = get(apkLibsPath + "libpairipcore.so", true)
        val stubStream = inputStreamFromBundledResource("pairip", "libpairipcore_stub.so")!!
        stubStream.use { input ->
            pairipLib.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        inputStreamFromBundledResource("pairip/apps", "$app.json")?.let { stream ->
            val json = stream.bufferedReader().use { it.readText() }
            val nativeLibMap = Json.decodeFromString<Map<String, LibPatchInfo>>(json)

            for ((libName, data) in nativeLibMap) {
                val lib = get(apkLibsPath + libName, true)
                val elf: ElfFile by lazy { ElfFile.from(lib) }

                // decrypt .text
                if (!data.keystream.isNullOrBlank()) {
                    val offset = elf.firstSectionByName(".text").header.sh_offset
                    decryptElf(lib, offset, data.keystream)
                }

                // patch .data
                if (!data.patch.isNullOrBlank()) {
                    val dataSection = elf.firstSectionByName(".data")
                    patchData(
                        lib,
                        dataSection.header.sh_offset,
                        dataSection.header.sh_size.toInt(),
                        data.patch
                    )
                }

                // fix GOT
                if (!data.relocations.isNullOrEmpty()) {
                    ElfPatcher.init(this)
                    val result =
                        ElfPatcher.patch(lib.path, data.relocations.toTypedArray())
                    if (!result)
                        throw Error("Error patching native library ($libName)")
                }
            }
        }
    }
}

fun decryptElf(
    file: File,
    offset: Long,
    base64Keystream: String
) {
    val keystreamBytes = Base64.getDecoder().decode(base64Keystream)

    RandomAccessFile(file, "rw").use { raf ->
        val channel: FileChannel = raf.channel
        channel.position(offset)

        val buffer = ByteBuffer.allocate(keystreamBytes.size)
        val bytesRead = channel.read(buffer)

        val fileBytes = buffer.array()
        for (i in 0 until bytesRead) {
            fileBytes[i] = (fileBytes[i].toInt() xor keystreamBytes[i].toInt()).toByte()
        }

        channel.position(offset)
        channel.write(ByteBuffer.wrap(fileBytes, 0, bytesRead))
    }
}

private fun decompressGzip(compressedData: ByteArray): ByteArray {
    ByteArrayInputStream(compressedData).use { inputStream ->
        GZIPInputStream(inputStream).use { gzipStream ->
            ByteArrayOutputStream().use { outputStream ->
                gzipStream.copyTo(outputStream)
                return outputStream.toByteArray()
            }
        }
    }
}

fun patchData(
    file: File,
    offset: Long,
    size: Int,
    base64IpsPatch: String
) {
    val zipped = Base64.getDecoder().decode(base64IpsPatch)
    val patch = decompressGzip(zipped)

    RandomAccessFile(file, "rw").use { raf ->
        raf.seek(offset)
        val data = ByteArray(size)
        raf.readFully(data)

        val patchedData = IpsPatcher.apply(data, patch)

        raf.seek(offset)
        raf.write(patchedData)
    }
}