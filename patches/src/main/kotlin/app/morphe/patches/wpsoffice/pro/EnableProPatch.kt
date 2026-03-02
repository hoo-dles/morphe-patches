package app.morphe.patches.wpsoffice.pro

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.wpsoffice.misc.antitamper.disableAntiTamperPatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
    description = "Enables app features locked behind the subscription paywall. Login is required and AI functionality is unavailable."
) {
    compatibleWith("cn.wps.moffice_eng"("18.24"))

    dependsOn(disableAntiTamperPatch)

    execute {
        HasPrivilegeFingerprint.method.returnEarly(true)
    }
}