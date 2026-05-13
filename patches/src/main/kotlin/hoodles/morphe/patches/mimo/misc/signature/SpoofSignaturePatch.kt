package hoodles.morphe.patches.mimo.misc.signature

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

const val SIGNATURE_SHA1 = "93D53764C40AEB53E09A306D01D74DFF11412021"

val spoofSignatureHeaderPatch = bytecodePatch {
    execute {
        SignatureBytesToStringFingerprint.method.returnEarly(SIGNATURE_SHA1)
        SignatureFromPackageFingerprint.method.returnEarly(SIGNATURE_SHA1)
    }
}