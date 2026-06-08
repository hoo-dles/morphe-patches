package hoodles.morphe.patches.duolingo.misc.integrity

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstruction
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import hoodles.morphe.patches.duolingo.shared.Constants

@Suppress("unused")
val disableLoginIntegrityPatch = bytecodePatch(
    name = "Disable Login Integrity",
    description = "Removes Play Integrity device attestation from login request."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val emptySignalRef = BasicLoginFingerprint.method.let {
            it.getInstruction<Instruction21c>(
                it.indexOfFirstInstruction(Opcode.SGET_OBJECT)
            ).getReference<FieldReference>()
        }
        val abstractSignalClass = classDefBy(emptySignalRef!!.type).superclass

        LoginStateFingerprint.method.apply {
            val setSignalIndex = indexOfFirstInstruction {
                this.getReference<FieldReference>()?.type == abstractSignalClass
            }
            val signalParamReg = this.getInstruction<Instruction22c>(setSignalIndex).registerA
            this.addInstruction(
                setSignalIndex, "sget-object v$signalParamReg, $emptySignalRef"
            )
        }
    }
}