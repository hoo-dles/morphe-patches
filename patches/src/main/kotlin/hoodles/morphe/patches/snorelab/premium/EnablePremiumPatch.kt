package hoodles.morphe.patches.snorelab.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(
        Compatibility(
            name = "SnoreLab",
            packageName = "com.snorelab.app",
            appIconColor = 0x1B5A9A,
            targets = listOf(AppTarget("2.27.3.6291"))
        )
    )

    execute {
        IsRemotePremiumActiveFingerprint.method.returnEarly(true)
        IsPremiumDateValidFingerprint.method.returnEarly(true)
    }
}