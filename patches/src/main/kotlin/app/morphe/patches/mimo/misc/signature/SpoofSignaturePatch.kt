package app.morphe.patches.mimo.misc.signature

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.mimo.shared.Constants
import app.morphe.util.returnEarly

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