package app.morphe.patches.lingory.premium

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.shared.misc.hex.hexPatch

@Suppress("unused")
val enablePremiumPatch = rawResourcePatch(
    name = "Enable Premium"
) {
    compatibleWith("org.languageapp.lingory"("1.2.75"))

    dependsOn(
        hexPatch(block = {
            // ldur     x1, [fp, #-0x10]
            // ldur     x0, [fp, #-0x18]   -->   add  x0, x22, #0x20
            // stur     w0, [x1, #0x37]
            "a1 03 5f f8 a0 83 5e f8 20 70 03 b8" asPatternTo "a1 03 5f f8 c0 82 00 91 20 70 03 b8" inFile "lib/arm64-v8a/libapp.so"
        })
    )
}