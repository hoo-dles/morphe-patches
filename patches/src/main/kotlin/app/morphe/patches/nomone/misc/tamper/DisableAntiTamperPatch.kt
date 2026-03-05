package app.morphe.patches.nomone.misc.tamper

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

val disableAntiTamperPatch = bytecodePatch(
    name = "Disable anti-tamper",
    description = "Disables anti-tamper checks including signature verification and purchase ID."
) {

    compatibleWith("nom.vrd"("1.9.3-GooglePlay"))

    execute {
        IsValidSignatureFingerprint.method.returnEarly(true)
        IsCrackedFingerprint.method.returnEarly(false)
    }
}