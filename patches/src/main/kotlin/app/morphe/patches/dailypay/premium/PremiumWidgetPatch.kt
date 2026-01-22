package app.morphe.patches.dailypay.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21t

internal val premiumWidgetPatch = bytecodePatch {
    execute {
        WidgetConfigOnCreateFingerprint.apply {
            val ifIndex = instructionMatches.last().index
            val ifRegister = method.getInstruction<Instruction21t>(ifIndex).registerA
            method.addInstruction(
                ifIndex,
                "const/4 v$ifRegister, 0x0")
        }
    }
}