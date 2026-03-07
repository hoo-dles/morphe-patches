package app.morphe.patches.nomone.misc.telemetry

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val disableTelemetryPatch = bytecodePatch(
    name = "Disable telemetry",
    description = "Disables event logging sent to the app's custom endpoint."
) {
    compatibleWith("nom.vrd"("1.9.3-GooglePlay", "1.9.3-storage"))

    execute {
        SendTelemetryEventFingerprint.method.returnEarly()
    }
}