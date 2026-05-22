package hoodles.morphe.patches.windy.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import hoodles.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import hoodles.morphe.patches.shared.misc.extension.sharedExtensionPatch

internal val extensionPatch = sharedExtensionPatch(
    "windy",
    activityOnCreateExtensionHook("/MainActivity;")
)

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables some app features locked behind the subscription paywall. Not all premium functionality is available."
) {
    compatibleWith(Compatibility(
        name = "Windy",
        packageName = "com.windyty.android",
        appIconColor = 0x9D0300,
        targets = listOf(AppTarget("50.0.2"))
    ))

    dependsOn(extensionPatch)

    execute {
        IsPremiumForWidgetFingerprint.method.returnEarly(true)

        ShouldInterceptRequestFingerprint.method.apply {
            val returnObjReg = getInstruction<OneRegisterInstruction>(instructions.size - 1).registerA

            addInstructions(instructions.size - 1, """
                invoke-static { p2, v$returnObjReg }, Lhoodles/morphe/extension/windy/premium/EnablePremiumPatch;->patchAppJavascript(Landroid/webkit/WebResourceRequest;Landroid/webkit/WebResourceResponse;)V
            """.trimIndent())
        }
    }
}