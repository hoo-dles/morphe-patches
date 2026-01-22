package app.morphe.patches.teuida.premium

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables premium paid subscription.",
) {
    compatibleWith("net.teuida.teuida")

    execute {
        PremiumGetterFingerprint.method.replaceInstruction(
            0,
            "sget-object v0, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;"
        )
    }
}