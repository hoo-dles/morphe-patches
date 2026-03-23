package app.morphe.patches.podcastaddict.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Podcast Addict",
        packageName = "com.bambuna.podcastaddict",
        targets = listOf(AppTarget("2026.1"))
    ))

    execute {
        HasPremiumFingerprint.method.returnEarly(true)
        IsValidSignatureFingerprint.method.returnEarly(true)
        IsValidPackageSourceFingerprint.method.returnEarly(true)
    }
}