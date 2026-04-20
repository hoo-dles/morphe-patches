package hoodles.morphe.patches.wpsoffice.misc.antitamper

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.wpsoffice.shared.Constants

val disableAntiTamperPatch = bytecodePatch(
    name = "Disable anti-tamper",
    description = "Disables various anti-tamper checks that causes the app to force-close."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        SecurityCheck1Fingerprint.method.returnEarly()
        SecurityCheck2Fingerprint.method.returnEarly()
    }
}