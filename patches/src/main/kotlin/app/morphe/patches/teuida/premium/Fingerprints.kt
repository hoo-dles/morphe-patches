package app.morphe.patches.teuida.premium

import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

object PremiumGetterFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Ljava/lang/Boolean;",
    custom = { method, classDef ->
        classDef.type == "Lnet/teuida/teuida/modelKt/MeData;" &&
                method.instructions.any { instr ->
                    instr.opcode == Opcode.IGET_OBJECT &&
                            ((instr as ReferenceInstruction).reference as FieldReference).name == "premium"
                }
    }
)
