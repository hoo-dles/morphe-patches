/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.protonvpn.lan

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.resource.ResourceType
import app.morphe.patcher.resource.resourceId
import com.android.tools.smali.dexlib2.iface.Field
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.value.IntEncodedValue
import hoodles.morphe.compatibility.Compat
import hoodles.morphe.patches.protonvpn.shared.misc.unlockfeatures.unlockFeaturesPatch

@Suppress("unused")
val unlockLanConnectionsPatch = bytecodePatch(
    name = "Unlock LAN connections",
    description = "Enables the LAN connections feature usually locked behind the Proton Plus paywall."
) {
    compatibleWith(Compat.PROTON_VPN)

    dependsOn(unlockFeaturesPatch)

    execute {
        val titleId = resourceId(ResourceType.STRING, "settings_advanced_allow_lan_description")
        var titleIdField: Field? = null
        classDefForEach { classDef ->
            classDef.staticFields.forEach { field ->
                if ((field.initialValue as? IntEncodedValue)?.value?.toLong() == titleId) {
                    titleIdField = field
                    return@classDefForEach
                }
            }
        }

        checkNotNull(titleIdField) { "Unable to find resource reference field" }

        getLanConnectionsSettingViewStateCtor(titleIdField).apply {
            val isRestrictedRegisterIndex = this.instructionMatches.last().index - 1
            val isRestrictedRegister = method.getInstruction<OneRegisterInstruction>(isRestrictedRegisterIndex).registerA

            method.replaceInstruction(isRestrictedRegisterIndex,"const/4 v$isRestrictedRegister, 0x0")
        }
    }
}