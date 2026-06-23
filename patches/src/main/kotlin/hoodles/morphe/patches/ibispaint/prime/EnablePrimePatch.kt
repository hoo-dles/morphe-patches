package hoodles.morphe.patches.ibispaint.prime

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.all.misc.hex.hexPatch

@Suppress("unused")
val enablePrimePatch = rawResourcePatch(
    name = "Enable Prime membership",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "IbisPaint X",
        packageName = "jp.ne.ibis.ibispaintx.app",
        appIconColor = 0x0B649B,
        targets = listOf(AppTarget("14.0.4"))
    ))

    dependsOn(
        hexPatch(block = {
            val libPath = "lib/arm64-v8a/libibispaint.so"
            
            // disable anti-tamper
            "08 AF 00 B0 08 D9 47 F9 08 FD DF 88 1F 05 00 71" asPatternTo "E0 03 1F 2A C0 03 5F D6 08 FD DF 88 1F 05 00 71" inFile libPath

            // enable prime membership
            "45 39 47 94 00 00 62 9E 00 21 60 1E E0 B7 9F 1A 02 00 00 14 E0 03 1F 2A" asPatternTo "45 39 47 94 00 00 62 9E 00 21 60 1E E0 B7 9F 1A 1F 20 03 D5 20 00 80 52" inFile libPath
        })
    )
}