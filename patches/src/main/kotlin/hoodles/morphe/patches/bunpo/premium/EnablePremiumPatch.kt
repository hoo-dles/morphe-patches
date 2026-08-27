/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.bunpo.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.patches.bunpo.shared.stripPairipPatch
import hoodles.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import hoodles.morphe.patches.shared.misc.extension.sharedExtensionPatch
import hoodles.morphe.util.requireArm64

val sharedExtensionPatch = sharedExtensionPatch(
    "bunpo",
    activityOnCreateExtensionHook("/SplashActivity;")
)

val enablePremiumPatch = bytecodePatch(
    name = "Enable Platinum",
    description = "Enables app features locked behind the subscription paywall. Requirements: arm64-v8a, strict apk version"
) {
    compatibleWith(Compatibility(
        name = "Bunpo",
        packageName = "com.bunpoapp",
        appIconColor = 0xfdfdfd,
        targets = listOf(AppTarget("3.21.0"))
    ))

    availability(requireArm64())

    dependsOn(stripPairipPatch, sharedExtensionPatch)

    execute {
        EntitlementInfosCtorFingerprint.method.addInstructions(0, """
            invoke-static {p1, p2}, Lhoodles/morphe/extension/bunpo/premium/EnablePremiumPatch;->addEntitlement(Ljava/util/Map;Lcom/revenuecat/purchases/VerificationResult;)V
        """.trimIndent())
    }
}