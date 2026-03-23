package app.morphe.patches.nomone.misc.telemetry

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.nomone.shared.Constants
import app.morphe.util.returnEarly

@Suppress("unused")
val disableTelemetryPatch = bytecodePatch(
    name = "Disable telemetry",
    description = "Disables event logging sent to the app's custom endpoint."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        SendTelemetryEventFingerprint.method.returnEarly()
    }
}