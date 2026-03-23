package app.morphe.patches.guessthecountry.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val EnablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "World Map Quiz",
        packageName = "com.qbis.guessthecountry",
        targets = listOf(AppTarget("3.34.2"))
    ))

    execute {
        IsProductInCacheFingerprint.method.returnEarly(true)
    }
}