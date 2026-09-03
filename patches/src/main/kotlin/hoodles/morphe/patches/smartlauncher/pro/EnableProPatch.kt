/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.smartlauncher.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import hoodles.morphe.patches.smartlauncher.misc.signature.disableSignatureCheckPatch
import hoodles.morphe.patches.smartlauncher.shared.Constants

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(disableSignatureCheckPatch)

    execute {
        PurchaseItemsCtor.apply {
            val sputInstr =  this.instructionMatches[2].getInstruction<Instruction21c>()
            val lifetimeField = sputInstr.getReference<FieldReference>()!!
            val purchasableItemType = classDefBy(lifetimeField.type)

            val singletonField = this.classDef.staticFields.first { f -> f.type == this.classDef.type }

            val itemSetMethod = PurchasableItemSetFingerprint.match(purchasableItemType).originalMethod
            val itemGetMethod = PurchasableItemGetFingerprint.match(purchasableItemType).originalMethod

            // call method to set "lifetime" PurchasableItem to true and trigger access changed intent
            this.method.addInstructionsWithLabels(
                this.method.instructions.size - 1,
                """
                    sget-object v0, $lifetimeField
                    invoke-virtual {v0}, $itemGetMethod
                    move-result v1
                    if-nez v1, :end
                    invoke-static {}, ${GetAppFingerprint.method}
                    move-result-object v1
                    const/4 v2, 0x1
                    invoke-virtual {v0, v1, v2}, $itemSetMethod
                    sget-object v0, $singletonField
                    invoke-static {v0, v1, v2}, ${HasChangedIntentFingerprint.method}
                """.trimIndent(),
                ExternalLabel("end", this.method.instructions.last())
            )
        }
    }
}