package app.morphe.patches.wpsoffice.pro

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.wpsoffice.misc.antitamper.disableAntiTamperPatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
    description = "Enables Pro subscription features. Login is required and AI functionality is unavailable."
) {
    compatibleWith("cn.wps.moffice_eng")

    dependsOn(disableAntiTamperPatch)

    execute {
        HasPrivilegeFingerprint.method.returnEarly(true)
    }
}