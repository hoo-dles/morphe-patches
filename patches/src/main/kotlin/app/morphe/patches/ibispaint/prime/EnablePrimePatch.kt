package app.morphe.patches.ibispaint.prime

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.shared.misc.hex.hexPatch

@Suppress("unused")
val enablePrimePatch = rawResourcePatch(
    name = "Enable Prime Membership"
) {
    compatibleWith("jp.ne.ibis.ibispaintx.app"("13.1.19"))

    dependsOn(
        hexPatch(block = {
            val libPath = "lib/arm64-v8a/libibispaint.so"
            
            // disable anti-tamper
            "88 9F 00 D0 08 89 45 F9 08 FD DF 88 1F 05 00 71" asPatternTo "E0 03 1F 2A C0 03 5F D6 08 FD DF 88 1F 05 00 71" inFile libPath

            // enable prime membership
            "48 69 41 94 00 00 62 9E 00 21 60 1E E0 B7 9F 1A 02 00 00 14 E0 03 1F 2A" asPatternTo "48 69 41 94 00 00 62 9E 00 21 60 1E E0 B7 9F 1A 1F 20 03 D5 20 00 80 52" inFile libPath
        })
    )
}