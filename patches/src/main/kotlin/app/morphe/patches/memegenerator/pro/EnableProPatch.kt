package app.morphe.patches.memegenerator.pro

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnBoxedBooleanEarly
import app.morphe.util.returnEarly

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Meme Generator",
        packageName = "com.zombodroid.MemeGenerator",
        targets = listOf(AppTarget("4.6670"))
    ))

    execute {
        CheckSignatures1Fingerprint.method.returnEarly(true)
        CheckSignatures2Fingerprint.method.returnBoxedBooleanEarly(true)

        IsFreeFingerprint.method.returnBoxedBooleanEarly(false)
        IsCacheLicenseValidFingerprint.method.returnEarly(true)
    }
}