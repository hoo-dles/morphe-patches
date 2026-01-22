package app.morphe.patches.avocards.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium"
) {
    compatibleWith("com.avocards")

    execute {
        GetPremiumUserFingerprint.method.returnEarly(true)
        GetPremiumBaseFingerprint.method.replaceInstruction(
            0,
            "sget-object v0, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;"
        )
        IsPremiumFingerprint.method.returnEarly(true)

        UserEntityCtorFingerprint.method.apply {
            this.addInstructions(
                this.instructions.count() - 1, """
                const/4 v1, 0x1
                iput-boolean v1, v0, Lcom/avocards/data/entity/UserEntity;->premium:Z
            """.trimIndent()
            )
        }
    }
}