package app.morphe.patches.memegenerator.pro

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnBoxedBooleanEarly
import app.morphe.util.returnEarly

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro"
) {
    compatibleWith("com.zombodroid.MemeGenerator")

    execute {
        CheckSignatures1Fingerprint.method.returnEarly(true)
        CheckSignatures2Fingerprint.method.returnBoxedBooleanEarly(true)

        IsFreeFingerprint.method.returnBoxedBooleanEarly(false)
        IsCacheLicenseValid.method.returnEarly(true)
    }
}