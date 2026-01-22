package app.morphe.patches.duolingo.shared

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstructionOrThrow
import app.morphe.util.toStringMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.StringReference

internal object Utils {
    // Gets field from toString() method with the following format:
    //  toString() { return "[Class]([field1]=" + this.a + "[field2]=" + this.b + ... + ")"; }
    internal fun ClassDef.fieldFromToString(subStr: String): FieldReference {
        val toString = this.toStringMethod()
        val strIndex = toString.indexOfFirstInstructionOrThrow() {
            this.opcode == Opcode.CONST_STRING &&
                    getReference<StringReference>()?.string?.contains(subStr) ?: false
        }
        // The iget-xxx should always be after const-string and invoke-virtual (StringBuilder.append())
        val field = toString.getInstruction<ReferenceInstruction>(strIndex + 2).getReference<FieldReference>()
        return field ?: throw PatchException("Could not find field: $subStr")
    }
}