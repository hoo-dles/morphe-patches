package app.morphe.patches.sofascore.ads

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable ads"
) {
    compatibleWith("com.sofascore.results")

    execute {
        GetForceAdsFingerprint.method.returnEarly(false)
        GetForceHideAdsFingerprint.method.returnEarly(true)
        GetHasServerAdsFingerprint.method.returnEarly(false)
    }
}