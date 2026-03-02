package app.morphe.patches.eggbun.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium"
) {
    compatibleWith("kr.eggbun.eggconvo"("4.12.19"))

    execute {
        IsLifetimePremiumFingerprint.method.returnEarly(true)
        GetExpiredFingerprint.method.returnEarly(false)
        GetLockedLessonRefFingerprint.method.returnEarly(false)
        GetLockedLessonDetailsFingerprint.method.returnEarly(false)
    }
}