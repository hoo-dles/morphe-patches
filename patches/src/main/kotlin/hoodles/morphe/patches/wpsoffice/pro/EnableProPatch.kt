package hoodles.morphe.patches.wpsoffice.pro

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.wpsoffice.misc.antitamper.disableAntiTamperPatch
import hoodles.morphe.patches.wpsoffice.shared.Constants

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
    description = "Enables app features locked behind the subscription paywall. Login is required and AI functionality is unavailable."
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(disableAntiTamperPatch)

    execute {
        HasPrivilegeFingerprint.method.returnEarly(true)
    }
}