/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.sofascore.ads

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.sofascore.shared.Constants.COMPATIBILITY

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable ads",
    description = "Disables all ads contained within the UI."
) {
    compatibleWith(COMPATIBILITY)

    execute {
        GetForceAdsFingerprint.method.returnEarly(false)
        GetForceHideAdsFingerprint.method.returnEarly(true)
        GetHasServerAdsFingerprint.method.returnEarly(false)
    }
}