package app.morphe.patches.solidexplorer.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Premium Pro"
) {
    compatibleWith("pl.solidexplorer2")

    execute {
        LicenseDetailsCtorFingerprint.method.apply {
            this.addInstructionsWithLabels(
                this.instructions.size - 1,
                """
                    invoke-virtual {p0}, Lpl/solidexplorer/licensing/LicenseDetails;->adsEnabled()Z
                    move-result v0
                    if-eqz v0, :end
                    sget-object v0, Lpl/solidexplorer/licensing/LicenseState;->PREMIUM_PRO:Lpl/solidexplorer/licensing/LicenseState;
                    iput-object v0, p0, Lpl/solidexplorer/licensing/LicenseDetails;->a:Lpl/solidexplorer/licensing/LicenseState;
                """.trimIndent(),
                ExternalLabel("end", this.instructions.last())
            )
        }
    }
}