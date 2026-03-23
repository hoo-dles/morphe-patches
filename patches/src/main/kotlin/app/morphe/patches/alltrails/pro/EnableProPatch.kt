package app.morphe.patches.alltrails.pro

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Peak membership",
    description = "Enables app features locked behind the subscription paywall.",
) {
    compatibleWith(
        Compatibility(
            name = "AllTrails",
            packageName = "com.alltrails.alltrails",
            targets = listOf(AppTarget("26.3.20"))
        )
    )

    execute {
        IsProFingerprint.method.returnEarly(true)
        GetSubscriptionTierFingerprint.method.returnEarly("peak")
    }
}