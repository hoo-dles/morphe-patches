/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.pairip.extension

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.reference.FieldReference


internal fun getExtensionPatch(appName: String) = bytecodePatch {
    extendWith("extensions/__generated__/$appName.mpe")

    // Fix any package names that had hyphens and need post-build patching
    execute {
        val pairipHookClass =
            mutableClassDefBy ("Lhoodles/morphe/extension/$appName/pairip/PairipHook;")
        val injectMethods = listOf("injectMethods", "injectStrings").map { methodName ->
            pairipHookClass.directMethods.first { it.name == methodName }
        }

        for (method in injectMethods) {
            for (i in 0 until method.instructions.count()) {
                val instr = method.getInstruction(i) as? Instruction21c
                if (instr == null || instr.opcode != Opcode.SPUT_OBJECT) continue

                val field = instr.getReference<FieldReference>()
                if (field != null && field.definingClass.contains("__HYPHEN__")) {
                    val fixedClassName = field.definingClass.replace("__HYPHEN__", "-")
                    method.replaceInstruction(
                        i, """
                            sput-object v${instr.registerA}, $fixedClassName->${field.name}:${field.type}
                        """.trimIndent()
                    )
                }
            }
        }
    }
}