/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.macrofactor.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.fix.changepackageinstaller.changePackageInstallerPatch
import hoodles.morphe.patches.macrofactor.misc.signature.spoofSignaturePatch
import hoodles.morphe.patches.macrofactor.shared.Constants
import hoodles.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import hoodles.morphe.patches.shared.misc.extension.sharedExtensionPatch
import hoodles.morphe.util.requireRootMount

internal val extensionPatch = sharedExtensionPatch(
    "macrofactor",
    activityOnCreateExtensionHook("/FlutterFragmentActivity;")
)

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium (ROOT)",
    description = "Enables app features locked behind the subscription paywall. Requirements: root mount"
) {
    compatibleWith(*Constants.COMPATIBILITY)

    availability(requireRootMount)

    dependsOn(extensionPatch, spoofSignaturePatch, changePackageInstallerPatch())

    execute {
        BuildCustomerInfoFingerprint.method.addInstructions(0, """
            invoke-static { p1 }, Lhoodles/morphe/extension/macrofactor/premium/EnablePremiumPatch;->updateCustomerInfo(Lorg/json/JSONObject;)V
        """.trimIndent())
    }
}