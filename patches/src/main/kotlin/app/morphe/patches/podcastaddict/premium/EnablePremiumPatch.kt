package app.morphe.patches.podcastaddict.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium"
) {
    compatibleWith("com.bambuna.podcastaddict")

    execute {
        HasPremiumFingerprint.method.returnEarly(true)
        IsValidSignatureFingerprint.method.returnEarly(true)
        IsValidPackageSourceFingerprint.method.returnEarly(true)
    }
}