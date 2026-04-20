package hoodles.morphe.patches.macrofactor.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.patches.macrofactor.misc.signature.spoofSignaturePatch
import hoodles.morphe.patches.macrofactor.shared.Constants
import hoodles.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import hoodles.morphe.patches.shared.misc.extension.sharedExtensionPatch

internal val extensionPatch = sharedExtensionPatch(
    "macrofactor",
    activityOnCreateExtensionHook("/FlutterFragmentActivity;")
)

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(*Constants.COMPATIBILITY)

    dependsOn(extensionPatch, spoofSignaturePatch)

    execute {
        BuildCustomerInfoFingerprint.method.addInstructions(0, """
            invoke-static { p1 }, Lhoodles/morphe/extension/macrofactor/premium/EnablePremiumPatch;->updateCustomerInfo(Lorg/json/JSONObject;)V
        """.trimIndent())
    }
}