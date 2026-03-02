package app.morphe.patches.iconpacker.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.pairip.license.disableLicenseCheckPatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

@Suppress("unused")
val unlockPremiumFeaturesPatch = bytecodePatch(
    name = "Unlock premium features",
    description = "Enables app features locked behind the subscription paywall. Some UI elements may not show an active membership, but this does not effect functionality."
) {
    compatibleWith("cn.ommiao.iconpacker"("1.21.0-release"))

    dependsOn(disableLicenseCheckPatch)

    execute {
        PurchaseUiStateCtorFingerprint.match(PurchaseUiStateFactoryFingerprint.classDef).apply {
            val hasMembershipDefaultValue = instructionMatches.first()
            val defaultValueReg = hasMembershipDefaultValue.getInstruction<OneRegisterInstruction>().registerA
            method.replaceInstruction(hasMembershipDefaultValue.index,
                "const/4 v$defaultValueReg, 0x1"
            )
        }

        ExportSetupFingerprint.apply {
            val setPremiumBoolInstruction = instructionMatches.first()
            val boolValueReg = setPremiumBoolInstruction.getInstruction<TwoRegisterInstruction>().registerA
            method.addInstruction(setPremiumBoolInstruction.index, "const/4 v$boolValueReg, 0x1")
        }
    }
}