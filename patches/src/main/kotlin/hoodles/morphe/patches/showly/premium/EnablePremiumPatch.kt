/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.showly.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.showly.shared.Constants
import hoodles.morphe.patches.showly.shared.spoofSignaturePatch

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Showly",
        packageName = Constants.PACKAGE_NAME,
        appIconColor = 0xf44336,
        targets = listOf(AppTarget("3.70.0"))
    ))

    dependsOn(spoofSignaturePatch)

    execute {
        IsPremiumFingerprint.method.returnEarly(true)
        GetVipFingerprint.method.returnEarly(true)
        CheckEntitlementsFingerprint.method.returnEarly()
    }
}