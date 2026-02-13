package app.morphe.patches.protonvpn.splittunneling

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

@Suppress("unused")
val unlockSplitTunnelingPatch = bytecodePatch(
    name = "Unlock split tunneling"
) {
    compatibleWith("ch.protonvpn.android")

    execute {
        SplitTunnelingSettingViewStateCtor.apply {
            val isRestrictedRegisterIndex = SplitTunnelingSettingViewStateCtor.instructionMatches.last().index - 1
            val isRestrictedRegister = method.getInstruction<OneRegisterInstruction>(isRestrictedRegisterIndex).registerA

            method.replaceInstruction(isRestrictedRegisterIndex,"const/4 v$isRestrictedRegister, 0x0")
        }

        ApplyRestrictionsFingerprint.apply {
            val isPlusUserIndex = instructionMatches.first().index - 1
            method.removeInstruction(isPlusUserIndex)
        }
    }
}