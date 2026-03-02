package app.morphe.patches.wpsoffice.misc.antitamper

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

val disableAntiTamperPatch = bytecodePatch(
    name = "Disable anti-tamper",
    description = "Disables various anti-tamper checks that causes the app to force-close."
) {
    compatibleWith("cn.wps.moffice_eng"("18.24"))

    execute {
        SecurityCheck1Fingerprint.method.returnEarly()
        SecurityCheck2Fingerprint.method.returnEarly()
    }
}