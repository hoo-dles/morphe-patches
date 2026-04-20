package hoodles.morphe.patches.xodo.pro

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.xodo.misc.signature.disableSignatureCheckPatch
import hoodles.morphe.patches.xodo.shared.Constants

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(disableSignatureCheckPatch)

    execute {
        IsProFingerprint.method.returnEarly(true)
    }
}