package hoodles.morphe.patches.showly.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Showly",
        packageName = "com.michaldrabik.showly2",
        appIconColor = 0xf44336,
        targets = listOf(AppTarget("3.70.0"))
    ))

    execute {
        IsPremiumFingerprint.method.returnEarly(true)
        GetVipFingerprint.method.returnEarly(true)
        CheckEntitlementsFingerprint.method.returnEarly()
    }
}