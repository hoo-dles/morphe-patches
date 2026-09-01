package hoodles.morphe.patches.aslbloom.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.patches.shared.revenuecat.getAddProductPatch

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "ASL Bloom",
        packageName = "com.toleio.us",
        appIconColor = 0xffcc87,
        targets = listOf(AppTarget("1.36.67"))
    ))

    dependsOn(getAddProductPatch("premium"))
}