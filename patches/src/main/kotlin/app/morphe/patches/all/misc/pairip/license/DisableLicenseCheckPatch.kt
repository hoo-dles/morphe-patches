package app.morphe.patches.all.misc.pairip.license

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.indexOfFirstInstruction
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import java.util.logging.Logger

@Suppress("unused")
val disableLicenseCheckPatch = bytecodePatch(
    name = "Disable Pairip license check",
    description = "Disables Play Integrity API (Pairip) client-side license check.",
    use = false
) {

    execute {
        if (ProcessLicenseResponseFingerprint.methodOrNull == null || ValidateLicenseResponseFingerprint.methodOrNull == null) {
            return@execute Logger.getLogger(this::class.java.name)
                .warning("Could not find Pairip licensing check. No changes applied.")
        }

        ProcessLicenseResponseFingerprint.apply {
            // Set first parameter (responseCode) to 0 (success status).
            method.addInstruction(0, "const/4 p1, 0x0")

            // Disable repeated check if present.
            RepeatedCheckFingerprint.matchOrNull(originalMethod)?.apply {
                val repeatedCheckFlagInstr = this.instructionMatches.first()
                val reg = repeatedCheckFlagInstr.getInstruction<OneRegisterInstruction>().registerA
                this.method.replaceInstruction(
                    // increase index by 1 to account for instruction added above
                    repeatedCheckFlagInstr.index + 1,
                    "const/4 v${reg}, 0x0"
                )
            }
        }

        // Short-circuit the license response validation.
        ValidateLicenseResponseFingerprint.method.returnEarly()
    }
}