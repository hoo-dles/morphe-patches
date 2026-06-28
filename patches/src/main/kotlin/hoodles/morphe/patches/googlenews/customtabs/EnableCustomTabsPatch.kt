package hoodles.morphe.patches.googlenews.customtabs

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import hoodles.morphe.patches.googlenews.shared.Constants
import hoodles.morphe.util.addInstructionsToEnd

@Suppress("unused")
val EnableCustomTabsPatch = bytecodePatch(
    name = "Enable custom tabs",
    description = "Open articles using your default browser."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val customTabsEnabledField = CustomTabsEnabledUsageFingerprint.instructionMatches
            .last().getFieldAccessed()
        val customTabsLauncherCtor = mutableClassDefBy(customTabsEnabledField.definingClass)
            .methods.first { it.name == "<init>" }

        customTabsLauncherCtor.addInstructionsToEnd("""
            const/4 v0, 0x1
            iput-boolean v0, p0, $customTabsEnabledField
        """.trimIndent())

        IsExperimentEnabledFingerprint.method.returnEarly(true)

        IsSupportedBrowserFingerprint.let {
            val resultIndex = it.instructionMatches.last().index
            val resultReg = it.method.getInstruction<OneRegisterInstruction>(resultIndex)
                .registerA
            it.method.addInstructions(resultIndex + 1, """
                const/4 v$resultReg, 0x1
            """.trimIndent())
        }
    }
}