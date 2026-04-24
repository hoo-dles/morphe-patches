package hoodles.morphe.patches.protonvpn.lan

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import hoodles.morphe.patches.protonvpn.shared.Constants

@Suppress("unused")
val unlockLanConnectionsPatch = bytecodePatch(
    name = "Unlock LAN connections",
    description = "Enables the LAN connections feature usually locked behind the Proton Plus paywall."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        LanConnectionsSettingViewStateCtor.apply {
            val isRestrictedRegisterIndex = LanConnectionsSettingViewStateCtor.instructionMatches.last().index - 1
            val isRestrictedRegister = method.getInstruction<OneRegisterInstruction>(isRestrictedRegisterIndex).registerA

            method.replaceInstruction(isRestrictedRegisterIndex,"const/4 v$isRestrictedRegister, 0x0")
        }

        ApplyRestrictionsFingerprint.apply {
            val isPlusUserIndex = instructionMatches.first().index - 1
            method.removeInstruction(isPlusUserIndex)
        }
    }
}