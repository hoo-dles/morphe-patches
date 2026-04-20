package hoodles.morphe.patches.lyfta.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.all.pairip.license.disableLicenseCheckPatch

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Lyfta",
        packageName = "com.lyfta",
        appIconColor = 0x000000,
        targets = listOf(AppTarget("1.551"))
    ))

    dependsOn(disableLicenseCheckPatch)

    execute {
        GetSubscriptionTypeFingerprint.method.returnEarly("premium")
    }
}