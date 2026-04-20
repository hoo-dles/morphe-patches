package hoodles.morphe.patches.xodo.misc.signature

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.xodo.shared.Constants

val disableSignatureCheckPatch = bytecodePatch(
    name = "Disable signature check",
    description = "Removes the anti-tamper protection, which verifies apk signature, causing the app to force close."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        SignatureCheckFingerprint.method.returnEarly()
    }
}