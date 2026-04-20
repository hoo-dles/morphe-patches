package hoodles.morphe.patches.soundcloud.misc.telemetry

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.soundcloud.shared.Constants

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