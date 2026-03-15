package app.morphe.patches.teuida.premium

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnBoxedBooleanEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith("net.teuida.teuida"("1.21.16"))

    execute {
        PremiumGetterFingerprint.method.returnBoxedBooleanEarly(true)
    }
}