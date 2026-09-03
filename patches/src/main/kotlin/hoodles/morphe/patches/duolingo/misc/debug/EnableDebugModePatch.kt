/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.duolingo.misc.debug

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.addInstructionsToEnd
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import hoodles.morphe.patches.duolingo.shared.Constants
import hoodles.morphe.patches.duolingo.shared.integrity.disableLoginIntegrityPatch
import hoodles.morphe.util.constructor

@Suppress("Unused")
val enableDebugModePatch = bytecodePatch(
    name = "Enable debug mode",
    description = "Enables hidden debug menu in settings.",
    default = false
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(disableLoginIntegrityPatch)

    execute {
        // Obfuscated class and name, but essentially: BuildConfigProvider.isDebug
        val isDebugFieldRef = BuildTargetFieldFingerprint.method
            .getInstruction(BuildTargetFieldFingerprint.instructionMatches.first().index + 1)
            .getReference<FieldReference>()
            ?: throw PatchException("Could not find isDebug field reference")

        val buildConfigProviderClass = mutableClassDefBy { it.type == isDebugFieldRef.definingClass }
        buildConfigProviderClass.constructor.addInstructionsToEnd(
            """
                const/4 v0, 0x1
                iput-boolean v0, p0, ${buildConfigProviderClass.type}->${isDebugFieldRef.name}:Z
            """.trimIndent()
        )
    }
}