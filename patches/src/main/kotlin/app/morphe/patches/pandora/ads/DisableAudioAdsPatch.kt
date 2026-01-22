package app.morphe.patches.pandora.ads

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val disableAudioAdsPatch = bytecodePatch(
    name = "Disable audio ads",
) {
    compatibleWith("com.pandora.android")

    execute {
        GetIsAdSupportedFingerprint.method.returnEarly(false)
        RequestAudioAdFingerprint.method.returnEarly()
    }
}
