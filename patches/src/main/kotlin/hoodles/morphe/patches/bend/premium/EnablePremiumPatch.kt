/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.bend.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.patches.bend.shared.pairip.stripPairipPatch
import hoodles.morphe.patches.shared.revenuecat.getAddEntitlementPatch
import hoodles.morphe.util.requireArm64


val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall. Requirements: arm64-v8a, strict apk version"
) {
    compatibleWith(Compatibility(
        name = "Bend",
        packageName = "com.bowerydigital.bend",
        appIconColor = 0x1674a8,
        targets = listOf(AppTarget("7.6.6"))
    ))

    availability(requireArm64())

    dependsOn(stripPairipPatch, getAddEntitlementPatch("premium"))
}