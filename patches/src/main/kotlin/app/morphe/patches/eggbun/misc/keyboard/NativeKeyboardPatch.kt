package app.morphe.patches.eggbun.misc.keyboard

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Force Native Keyboard",
    description = "When typing in normal lessons, Eggbun forces you to use their own on-screen keyboard. This patches forces the use of the default OS keyboard."
) {
    compatibleWith("kr.eggbun.eggconvo")

    execute {
        KrKeyboardCtorFingerprint.method.apply {
            this.addInstructions(this.instructions.count() - 1, """
                const/4 v0, 0x1
                iput-boolean v0, p0, Lcom/eggbun/chat2learn/ui/keyboard/KoreanKeyboardDelegateImpl;->isDefaultKeyboard:Z
            """.trimIndent())
        }
    }
}