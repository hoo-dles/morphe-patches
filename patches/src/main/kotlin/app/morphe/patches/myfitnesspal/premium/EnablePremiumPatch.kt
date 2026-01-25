package app.morphe.patches.myfitnesspal.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium+"
) {
    compatibleWith("com.myfitnesspal.android")

    execute {
        GetPremiumPlusFingerprint.method.returnEarly(true)
    }
}