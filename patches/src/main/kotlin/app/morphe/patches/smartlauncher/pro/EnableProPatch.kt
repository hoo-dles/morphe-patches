package app.morphe.patches.smartlauncher.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import app.morphe.patches.smartlauncher.misc.signature.disableSignatureCheckPatch
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
) {
    compatibleWith("ginlemon.flowerfree")

    dependsOn(disableSignatureCheckPatch)

    execute {
        PurchaseItemsCtor.apply {
            val sputInstr =  this.instructionMatches[2].getInstruction<Instruction21c>()
            val purchasableItemField = sputInstr.getReference<FieldReference>()!!
            val purchasableItemType = classDefBy(purchasableItemField.type)

            val itemSetMethod = PurchasableItemSetFingerprint.match(purchasableItemType).originalMethod
            val itemGetMethod = PurchasableItemGetFingerprint.match(purchasableItemType).originalMethod

            // call method to set "lifetime" PurchasableItem to true and trigger access changed intent
            this.method.addInstructionsWithLabels(
                this.method.instructions.size - 1,
                """
                    invoke-virtual {v0}, ${purchasableItemField.type}->${itemGetMethod.name}()Z
                    move-result v0
                    if-nez v0, :end
	                sget-object v0, ${this.classDef.type}->${purchasableItemField.name}:${purchasableItemField.type}
                    invoke-static {}, ${GetAppFingerprint.classDef.type}->${GetAppFingerprint.method.name}()${GetAppFingerprint.method.returnType}
                    move-result-object v1
                    const/4 v2, 0x1
                    invoke-virtual {v0, v1, v2}, ${purchasableItemField.type}->${itemSetMethod.name}(Landroid/content/Context;Z)V
                """.trimIndent(),
                ExternalLabel("end", this.method.instructions.last())
            )
        }
    }
}