package hoodles.morphe.patches.mimo.misc.signature

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.mimo.shared.Constants

val spoofSignatureHeaderPatch = bytecodePatch (
    name = "Spoof package signature",
    description = "Spoofs the SHA1 signature hash required for Firebase API calls."
){
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        SignatureBytesToStringFingerprint.method.returnEarly(Constants.SIGNATURE)
        SignatureFromPackageFingerprint.method.returnEarly(Constants.SIGNATURE)
    }
}