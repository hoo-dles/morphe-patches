package app.morphe.patches.wpsoffice.misc.antitamper

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

val disableAntiTamperPatch = bytecodePatch(
    name = "Disable anti-tamper checks"
) {
    compatibleWith("cn.wps.moffice_eng")

    execute {
        SecurityCheck1Fingerprint.method.returnEarly()
        SecurityCheck2Fingerprint.method.returnEarly()
    }
}