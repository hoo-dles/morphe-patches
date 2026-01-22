package app.morphe.patches.crunchyroll.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables client-side only premium features. Most functionality is absent as it is verified on the server."
) {
    compatibleWith("com.crunchyroll.crunchyroid")

    execute {
        HasPremiumBenefitFingerprint.method.returnEarly(true)
    }
}