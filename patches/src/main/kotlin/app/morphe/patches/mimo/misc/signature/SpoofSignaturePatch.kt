package app.morphe.patches.mimo.misc.signature

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

const val SIGNATURE = "93D53764C40AEB53E09A306D01D74DFF11412021"

val spoofSignatureHeaderPatch = bytecodePatch (
    name = "Spoof package signature"
){
    compatibleWith("com.getmimo"("9.0"))

    execute {
        SignatureBytesToStringFingerprint.method.returnEarly(SIGNATURE)
        SignatureFromPackageFingerprint.method.returnEarly(SIGNATURE)
    }
}