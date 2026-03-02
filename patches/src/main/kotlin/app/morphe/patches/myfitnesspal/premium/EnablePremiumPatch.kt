package app.morphe.patches.myfitnesspal.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium+",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith("com.myfitnesspal.android"("25.50.0"))

    execute {
        GetPremiumPlusFingerprint.method.returnEarly(true)
    }
}