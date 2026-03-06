package app.morphe.patches.nomone.trial

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.nomone.misc.tamper.disableAntiTamperPatch
import app.morphe.util.findFreeRegister
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

@Suppress("unused")
val removeTrialLimitPatch = bytecodePatch(
    name = "Remove trial limit",
    description = "Removes the imposed 6-hour trial usage limit."
) {

    compatibleWith("nom.vrd"("1.9.3-GooglePlay"))
    dependsOn(disableAntiTamperPatch)

    execute {
        val purchaseInfoStatusField = PurchaseInfoUsageFingerprint.instructionMatches.last().instruction
            .getReference<FieldReference>()!!

        GetPurchaseInfoFingerprint.apply {
            val returnMatch = instructionMatches.first()
            val returnReg = returnMatch.getInstruction<OneRegisterInstruction>().registerA
            val valueReg = method.findFreeRegister(returnMatch.index)

            method.addInstructions(returnMatch.index, """
                const/4 v$valueReg, 0x4
                iput v$valueReg, v$returnReg, ${purchaseInfoStatusField.definingClass}->${purchaseInfoStatusField.name}:I
            """.trimIndent())
        }
    }
}