/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.alltrails.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.util.returnEarly
import hoodles.morphe.patches.alltrails.shared.Constants
import java.util.logging.Logger

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Peak membership",
    description = "Enables some app features locked behind the subscription paywall. " +
        "Not all premium functionality is available. " +
        "26.7.40 uses an additional user-data delegate hook alongside the Gson User model."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val logger = Logger.getLogger("EnableProPatch")
        var applied = 0

        fun tryHook(label: String, method: MutableMethod?, block: (MutableMethod) -> Unit) {
            if (method == null) {
                logger.warning("Could not find $label; skipping")
                return
            }
            if (method.implementation == null) {
                logger.warning("Skipping $label: method has no implementation")
                return
            }
            try {
                block(method)
                applied++
                logger.info("Applied Peak hook: $label")
            } catch (e: Exception) {
                logger.warning("Failed to apply $label: ${e.message}")
            }
        }

        tryHook("User.isPro", IsProFingerprint.methodOrNull) { it.returnEarly(true) }
        tryHook("User.getSubscriptionTier", GetSubscriptionTierFingerprint.methodOrNull) {
            it.returnEarly("peak")
        }

        tryHook("UserDataDelegate.subscriptionInfo", Cb60SubscriptionInfoFingerprint.methodOrNull) {
            it.addInstructions(
                0,
                """
                sget-object v0, Lps10${'$'}d${'$'}c;->INSTANCE:Lps10${'$'}d${'$'}c;
                const/4 v1, 0x0
                const/4 v2, 0x0
                const/4 v3, 0x0
                new-instance v4, Lab60${'$'}a;
                invoke-direct {v4, v0, v1, v2, v3}, Lab60${'$'}a;-><init>(Lps10;ZZZ)V
                return-object v4
                """.trimIndent()
            )
        }

        if (applied == 0) {
            error("Enable Peak membership: no hooks applied")
        }
    }
}
