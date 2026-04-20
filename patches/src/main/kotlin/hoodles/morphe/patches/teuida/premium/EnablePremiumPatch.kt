package hoodles.morphe.patches.teuida.premium

import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.patches.teuida.shared.Constants
import hoodles.morphe.util.returnBoxedBooleanEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        PremiumGetterFingerprint.method.returnBoxedBooleanEarly(true)
    }
}