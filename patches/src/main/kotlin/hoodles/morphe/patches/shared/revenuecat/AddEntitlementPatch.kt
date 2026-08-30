package hoodles.morphe.patches.shared.revenuecat

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.extension.ExtensionHook
import hoodles.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import hoodles.morphe.patches.shared.misc.extension.sharedExtensionPatch


fun getAddEntitlementPatch(entitlementName: String, activityOnCreateExtensionHook: ExtensionHook = activityOnCreateExtensionHook()) = bytecodePatch {
    val sharedExtensionPatch = sharedExtensionPatch(
        "common/revenuecat",
        activityOnCreateExtensionHook
    )
    dependsOn(sharedExtensionPatch)

    execute {
        EntitlementInfosCtorFingerprint.method.addInstructions(0, """
            const-string v0, "$entitlementName"
            invoke-static {p1, p2, v0}, Lhoodles/morphe/extension/revenuecat/entitlement/EntitlementHelper;->addEntitlement(Ljava/util/Map;Lcom/revenuecat/purchases/VerificationResult;Ljava/lang/String;)V
        """.trimIndent())
    }
}