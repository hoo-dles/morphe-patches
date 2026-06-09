package hoodles.morphe.patches.myfitnesspal.premium

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
        appIconColor = 0x0072BC,
        targets = listOf(AppTarget("26.21.1"))
    ))

    execute {
        GetPremiumPlusFingerprint.method.returnEarly(true)
    }
}