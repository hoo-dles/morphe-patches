/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.smartlauncher.misc.signature

import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val disableSignatureCheckPatch = bytecodePatch {
    execute {
        // Let's just remove the System.exit call
        SignatureCheckFingerprint.apply {
            val exitIndex = this.instructionMatches.first().index
            this.method.removeInstruction(exitIndex)
        }
    }
}