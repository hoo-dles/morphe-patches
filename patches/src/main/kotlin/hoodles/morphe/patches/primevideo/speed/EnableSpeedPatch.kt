package hoodles.morphe.patches.primevideo.speed

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.primevideo.shared.Constants

@Suppress("unused")
val playbackSpeedPatch = bytecodePatch(
    name = "Enable speed control",
    description = "Enables experimental speed control to the video player.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        IsPlaybackSettingsV2EnabledFingerprint.method.returnEarly(true)
        IsPlaybackSpeedFeatureEnabledFingerprint.method.returnEarly(true)
    }
}