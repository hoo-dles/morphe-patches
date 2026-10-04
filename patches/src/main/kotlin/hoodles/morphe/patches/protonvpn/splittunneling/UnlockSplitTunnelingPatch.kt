/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.protonvpn.splittunneling

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import hoodles.morphe.compatibility.Compat
import hoodles.morphe.patches.protonvpn.shared.misc.unlockfeatures.unlockFeaturesPatch

@Suppress("unused")
val unlockSplitTunnelingPatch = bytecodePatch(
    name = "Unlock split tunneling",
    description = "Enables the split tunneling feature usually locked behind the Proton Plus paywall."
) {
    compatibleWith(Compat.PROTON_VPN)

    dependsOn(unlockFeaturesPatch)

    execute {
        SplitTunnelingSettingViewStateCtor.apply {
            val isRestrictedRegisterIndex = SplitTunnelingSettingViewStateCtor.instructionMatches.last().index - 1
            val isRestrictedRegister = method.getInstruction<OneRegisterInstruction>(isRestrictedRegisterIndex).registerA

            method.replaceInstruction(isRestrictedRegisterIndex,"const/4 v$isRestrictedRegister, 0x0")
        }
    }
}