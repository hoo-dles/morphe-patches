package app.morphe.patches.sofascore.ads

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable ads",
    description = "Disables all ads contained within the UI."
) {
    compatibleWith("com.sofascore.results"("25.12.17"))

    execute {
        GetForceAdsFingerprint.method.returnEarly(false)
        GetForceHideAdsFingerprint.method.returnEarly(true)
        GetHasServerAdsFingerprint.method.returnEarly(false)
    }
}