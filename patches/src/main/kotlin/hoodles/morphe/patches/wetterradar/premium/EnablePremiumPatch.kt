/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.wetterradar.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.compatibility.Compat

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables premium features: ad-free experience, HD radar and all gated content. Works without an account."
) {
    compatibleWith(Compat.WEATHER_RADAR)

    execute {
        // The isPro flow is consumed by every feature and ad gate — force it to true.
        FusedAccessCombineFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x1
                invoke-static {v0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
                move-result-object v0
                return-object v0
            """.trimIndent()
        )

        // Report SUBSCRIPTION as the AccessLevel so no paywall/upgrade UI is shown.
        AccessLevelFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Lo6;->b:Lo6;
                return-object v0
            """.trimIndent()
        )
    }
}
