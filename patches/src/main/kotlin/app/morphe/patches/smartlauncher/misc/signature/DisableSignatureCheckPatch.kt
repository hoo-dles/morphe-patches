package app.morphe.patches.smartlauncher.misc.signature

import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val disableSignatureCheckPatch = bytecodePatch(
    name = "Disable signature check",
    description = "Removes the anti-tamper protection, which verifies apk signature, causing the app to force close."
) {
    compatibleWith("ginlemon.flowerfree"("6.6 build 002"))

    execute {
        // Let's just remove the System.exit call
        SignatureCheckFingerprint.apply {
            val exitIndex = this.instructionMatches.first().index
            this.method.removeInstruction(exitIndex)
        }
    }
}