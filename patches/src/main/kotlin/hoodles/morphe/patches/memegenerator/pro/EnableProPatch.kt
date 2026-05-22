package hoodles.morphe.patches.memegenerator.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import hoodles.morphe.util.returnBoxedBooleanEarly

const val SIGNATURE_SHA1 = "72:D5:86:CB:B6:55:46:5D:64:07:87:68:AF:77:5D:A5:E6:0B:91:C9"

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Meme Generator",
        packageName = "com.zombodroid.MemeGenerator",
        appIconColor = 0xC93133,
        targets = listOf(AppTarget("4.6826"))
    ))

    execute {
        IsFreeFingerprint.method.returnBoxedBooleanEarly(false)
        IsCacheLicenseValidFingerprint.method.returnEarly(true)
        GetSignatureFingerprint.method.returnEarly(SIGNATURE_SHA1)

        LicenseCheckFingerprint.apply {
            val jgzIndex = LicenseCheckFingerprint.instructionMatches.last().index
            val jgzReg = LicenseCheckFingerprint.method
                .getInstruction<OneRegisterInstruction>(jgzIndex).registerA
            method.addInstructions(jgzIndex, "const/4 v$jgzReg, -1")
        }
    }
}