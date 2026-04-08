package app.morphe.patches.xodo.misc.signature

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.xodo.shared.Constants
import app.morphe.util.returnEarly

val disableSignatureCheckPatch = bytecodePatch(
    name = "Disable signature check",
    description = "Removes the anti-tamper protection, which verifies apk signature, causing the app to force close."
) {

    compatibleWith(Constants.COMPATIBILITY)

    execute {
        SignatureCheckFingerprint.method.returnEarly()
    }
}