/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.ibispaint.prime

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.util.byteArrayOf
import hoodles.morphe.util.requireArm64
import net.fornwall.jelf.ElfFile
import net.fornwall.jelf.ElfSymbol
import java.io.RandomAccessFile

@Suppress("unused")
val enablePrimePatch = rawResourcePatch(
    name = "Enable Prime membership",
    description = "Enables app features locked behind the subscription paywall. Requirements: arm64-v8a"
) {
    compatibleWith(Compatibility(
        name = "IbisPaint X",
        packageName = "jp.ne.ibis.ibispaintx.app",
        appIconColor = 0x0B649B,
        targets = listOf(AppTarget("14.0.11"))
    ))

    availability(requireArm64())

    execute {
        val lib = get("lib/arm64-v8a/libibispaint.so", true)
        val elf = ElfFile.from(lib)

        val isFailure = elf.dynamicSymbolTableSection.symbols.first {
            (it.name?.contains("isFailure")  ?: false) && it.type == ElfSymbol.STT_FUNC.toInt()
        }
        val isFailurePatch = byteArrayOf(
            "e0 03 1f 2a " +    // mov w0, wzr
            "c0 03 5f d6"       // ret
        )

        val isPromo = elf.dynamicSymbolTableSection.symbols.first {
            (it.name?.contains("isPromotionalPrimeMember") ?: false) && it.type == ElfSymbol.STT_FUNC.toInt()
        }
        val isPromoPatch = byteArrayOf(
            "20 00 80 52" +     // mov w0, #1
            "C0 03 5F D6"       // ret
        )

        val bytePatches = mapOf(
            isFailure.st_value to isFailurePatch,
            isPromo.st_value to isPromoPatch
        )

        RandomAccessFile(lib, "rw").use {
            bytePatches.forEach { (offset, bytes) ->
                it.seek(offset)
                it.write(bytes)
            }
        }
    }
}