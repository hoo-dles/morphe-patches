package app.morphe.patches.eggbun.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.eggbun.shared.Constants
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        IsLifetimePremiumFingerprint.method.returnEarly(true)
        GetExpiredFingerprint.method.returnEarly(false)
        GetLockedLessonRefFingerprint.method.returnEarly(false)
        GetLockedLessonDetailsFingerprint.method.returnEarly(false)
    }
}