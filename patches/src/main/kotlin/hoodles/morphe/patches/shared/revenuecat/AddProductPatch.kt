package hoodles.morphe.patches.shared.revenuecat

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.extension.ExtensionHook
import app.morphe.patches.all.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.all.misc.extension.sharedExtensionPatch

fun getAddProductPatch(productName: String, activityOnCreateExtensionHook: ExtensionHook = activityOnCreateExtensionHook()) = bytecodePatch {
    val sharedExtensionPatch = sharedExtensionPatch(
        "common/revenuecat",
        activityOnCreateExtensionHook
    )
    dependsOn(sharedExtensionPatch)

    execute {
        CustomerInfoCtorFingerprint.method.addInstructions(0, """
            const-string v0, "$productName"
            invoke-static {p2, v0}, Lhoodles/morphe/extension/revenuecat/PurchaseHelper;->addProduct(Ljava/util/Map;Ljava/lang/String;)V
        """.trimIndent())
    }
}