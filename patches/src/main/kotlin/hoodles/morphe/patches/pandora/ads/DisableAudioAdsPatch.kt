package hoodles.morphe.patches.pandora.ads

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.pandora.shared.Constants

@Suppress("unused")
val disableAudioAdsPatch = bytecodePatch(
    name = "Disable ads",
    description = "Disables ads during audio streaming."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        GetIsAdSupportedFingerprint.method.returnEarly(false)
        RequestAudioAdFingerprint.method.returnEarly()
    }
}
