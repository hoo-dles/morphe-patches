/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.lightroom.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.lightroom.shared.Constants
import hoodles.morphe.patches.lightroom.shared.stripPairipPatch
import hoodles.morphe.util.requireArm64

val enablePremiumPatch = bytecodePatch(
    name = "Unlock premium features",
    description = "Enables app features locked behind the subscription paywall. Requirements: arm64-v8a, strict apk version"
) {
    compatibleWith(Constants.COMPATIBILITY)

    availability(requireArm64())

    dependsOn(stripPairipPatch)

    execute {
        // enable "Limited-Time Premium Unlock"
        IsLTPUActiveFingerprint.method.returnEarly(true)
    }
}