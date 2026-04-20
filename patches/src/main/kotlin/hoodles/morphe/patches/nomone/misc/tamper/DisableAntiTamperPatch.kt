package hoodles.morphe.patches.nomone.misc.tamper

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.nomone.shared.Constants

val disableAntiTamperPatch = bytecodePatch(
    name = "Disable anti-tamper",
    description = "Disables anti-tamper checks including signature verification and purchase ID."
) {

    compatibleWith(Constants.COMPATIBILITY)

    execute {
        IsValidSignatureFingerprint.method.returnEarly(true)
        IsCrackedFingerprint.method.returnEarly(false)
    }
}