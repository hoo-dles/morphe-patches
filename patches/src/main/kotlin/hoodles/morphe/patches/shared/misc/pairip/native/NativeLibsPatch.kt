/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.pairip.native

import app.morphe.patcher.logging.Logger
import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.util.inputStreamFromBundledResource
import kotlinx.serialization.json.Json
import net.fornwall.jelf.ElfFile
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.Base64

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

                // decrypt .text
                if (data.keystream.isNotEmpty()) {
                    val elf = ElfFile.from(lib)
                    val offset = elf.firstSectionByName(".text").header.sh_offset
                    decryptElf(lib, offset, data.keystream)
                }

                // fix GOT
                if (data.relocations.isNotEmpty()) {
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