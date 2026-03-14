package app.morphe.patches.allinonecalculator.ads

import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable ads",
    description = "Disables all ads, although some layout placeholder elements may remain."
) {
    compatibleWith("all.in.one.calculator"("3.2.4"))

    execute {
        RegisterPluginsFingerprint.apply {
            val addPluginIndex = instructionMatches.last().index
            method.removeInstruction(addPluginIndex)
        }
    }
}