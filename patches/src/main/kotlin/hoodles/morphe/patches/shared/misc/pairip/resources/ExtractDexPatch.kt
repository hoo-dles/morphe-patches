/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.pairip.resources

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.util.toInt
import hoodles.morphe.util.find
import java.io.ByteArrayInputStream
import java.io.File

val ASSET_MAGIC = byteArrayOf(0x00, 0x49, 0x41, 0x50, 0x02)
val DEX_HEADER = byteArrayOf(0x64, 0x65, 0x78, 0x0a)

val extractedDexStreams: MutableList<ByteArrayInputStream> = mutableListOf()
val pairipAssets: MutableList<File> = mutableListOf()

internal val extractDexPatch = rawResourcePatch {
    execute {
        val assets = get("assets").listFiles()
        val dexBlobs = assets?.asSequence()?.mapNotNull { file ->
            if (!file.isFile)
                return@mapNotNull null

            file.inputStream().use { stream ->
                val header = ByteArray(ASSET_MAGIC.size)
                val bytesRead = stream.read(header)

                // check if file is a pairip asset
                if (bytesRead != ASSET_MAGIC.size || !header.contentEquals(ASSET_MAGIC))
                    return@mapNotNull null

                pairipAssets.add(file)

                // load rest of file into memory and search for dex header
                val contents = stream.readBytes()
                val dexOffset = contents.find(DEX_HEADER)
                if (dexOffset == -1)
                    return@mapNotNull null

                // dex size is LE int32 at: header_offset + 0x20
                val dexSize = contents.copyOfRange(dexOffset + 0x20, dexOffset + 0x24)
                    .toInt(littleEndian = true)

                return@mapNotNull contents.copyOfRange(dexOffset, dexOffset + dexSize)
            }
        }

        extractedDexStreams.addAll(dexBlobs?.map { it.inputStream() }?.toList() ?: emptyList())
    }
}