package hoodles.morphe.patches.lightroom.misc.version

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val disableVersionCheckPatch = bytecodePatch(
    name = "Disable version check",
    description = "Disables the server-side version check that prevents the app from starting."
) {
    compatibleWith(Compatibility(
        name = "Lightroom",
        packageName = "com.adobe.lrmobile",
        appIconColor = 0x31A8FF,
        targets = listOf(AppTarget("9.3.0"))
    ))

    execute {
        // Get the index of the IGET instruction (last matched opcode).
        val igetIndex = RefreshRemoteConfigurationMethodFingerprint.instructionMatches.last().index

        // This value represents the server command to clear all version restrictions.
        val statusForceReset = "-0x2"
        RefreshRemoteConfigurationMethodFingerprint.method.replaceInstruction(
            igetIndex,
            "const/4 v1, $statusForceReset"
        )
    }
}
