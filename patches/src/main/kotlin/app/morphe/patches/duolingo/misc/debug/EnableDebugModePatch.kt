package app.morphe.patches.duolingo.misc.debug

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.addInstructionsToEnd
import app.morphe.util.constructor
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

@Suppress("Unused")
val enableDebugModePatch = bytecodePatch(
    name = "Enable debug mode",
    description = "Enables hidden debug menu in settings.",
    use = false
) {
    compatibleWith("com.duolingo")

    execute {
        // Obfuscated class and name, but essentially: BuildConfigProvider.isDebug
        val isDebugFieldRef = BuildTargetFieldFingerprint.method
            .getInstruction(BuildTargetFieldFingerprint.instructionMatches.first().index + 1)
            .getReference<FieldReference>()
            ?: throw PatchException("Could not find isDebug field reference")

        val buildConfigProviderClass = mutableClassDefBy { it.type == isDebugFieldRef.definingClass }
        buildConfigProviderClass.constructor().addInstructionsToEnd(
            """
                const/4 v0, 0x1
                iput-boolean v0, p0, ${buildConfigProviderClass.type}->${isDebugFieldRef.name}:Z
            """.trimIndent()
        )
    }
}