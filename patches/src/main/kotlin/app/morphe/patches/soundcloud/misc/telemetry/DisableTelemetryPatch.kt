package app.morphe.patches.soundcloud.misc.telemetry

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.soundcloud.shared.Constants
import app.morphe.util.returnEarly

@Suppress("unused")
val disableTelemetryPatch = bytecodePatch(
    name = "Disable telemetry",
    description = "Disables SoundCloud's telemetry system."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        HandleMessageFingerprint.method.returnEarly()
    }
}