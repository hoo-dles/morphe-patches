package app.morphe.patches.xodo.pro

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.xodo.misc.signature.disableSignatureCheckPatch
import app.morphe.patches.xodo.shared.Constants
import app.morphe.util.returnEarly


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