package hoodles.morphe.patches.nova.prime

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

@Suppress("unused")
val enablePrimePatch = bytecodePatch(
    name = "Enable Prime",
    description = "Enable Nova Launcher Prime and app locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Nova Launcher",
        packageName = "com.teslacoilsw.launcher",
        targets = listOf(AppTarget("81042 (8.5.1)")),
        appIconColor = 0xDA4624
    ))

    execute {
        SetPrimeFromPreferencesFingerprint.apply {
            val primeReg = instructionMatches.last().getInstruction<OneRegisterInstruction>().registerA
            method.addInstructions(instructionMatches.last().index + 1, """
                const/16 v$primeReg, 0x200
            """.trimIndent())
        }
    }
}