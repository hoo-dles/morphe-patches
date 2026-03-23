package app.morphe.patches.myfitnesspal.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium+",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "MyFitnessPal",
        packageName = "com.myfitnesspal.android",
        targets = listOf(AppTarget("25.50.0"))
    ))

    execute {
        GetPremiumPlusFingerprint.method.returnEarly(true)
    }
}