package app.morphe.patches.wallcraft.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith("com.wallpaperscraft.wallpaper"("3.61.01"))

    execute {
        GetSubscriptionStateFingerprint.method.addInstructions(0,"""
            new-instance v0, Lcom/wallpaperscraft/billing/core/InfiniteSubscription;
            invoke-direct {v0}, Lcom/wallpaperscraft/billing/core/InfiniteSubscription;-><init>()V
            return-object v0
        """.trimIndent())
    }
}