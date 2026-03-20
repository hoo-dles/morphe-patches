package app.morphe.patches.windy.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.shared.misc.extension.sharedExtensionPatch
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

internal val extensionPatch = sharedExtensionPatch(
    "windy",
    activityOnCreateExtensionHook("/MainActivity;")
)

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables some app features locked behind the subscription paywall. Not all premium functionality is available."
) {
    compatibleWith("com.windyty.android"("49.0.1"))

    dependsOn(extensionPatch)

    execute {
        IsPremiumForWidgetFingerprint.method.returnEarly(true)

        ShouldInterceptRequestFingerprint.method.apply {
            val returnObjReg = getInstruction<OneRegisterInstruction>(instructions.size - 1).registerA

            addInstructions(instructions.size - 1, """
                invoke-static { p2, v$returnObjReg }, Lapp/morphe/extension/windy/premium/EnablePremiumPatch;->patchAppJavascript(Landroid/webkit/WebResourceRequest;Landroid/webkit/WebResourceResponse;)V
            """.trimIndent())
        }
    }
}