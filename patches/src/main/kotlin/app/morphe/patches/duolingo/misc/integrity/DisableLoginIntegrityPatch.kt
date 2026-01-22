package app.morphe.patches.duolingo.misc.integrity

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.indexOfFirstInstruction
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c

@Suppress("unused")
val disableLoginIntegrityPatch = bytecodePatch(
    name = "Disable Login Integrity",
    description = "Removes Play Integrity device attestation from login request."
) {
    compatibleWith("com.duolingo")

    execute {
        val emptySignalRef = BasicLoginFingerprint.method.let {
            it.getInstruction<Instruction21c>(
                it.indexOfFirstInstruction(Opcode.SGET_OBJECT)
            ).reference
        }

        LoginStateFingerprint.method.apply {
            val signalNullCheckIndex =
                this.indexOfFirstInstruction(LoginStateFingerprint.stringMatches.last().index, Opcode.INVOKE_STATIC)
            val signalParamNullCheckInstr = this.getInstruction<Instruction35c>(signalNullCheckIndex)
            val signalParamReg = this.getInstruction<Instruction35c>(signalNullCheckIndex).registerC

            this.addInstruction(
                0, "sget-object v$signalParamReg, $emptySignalRef"
            )
        }
    }
}