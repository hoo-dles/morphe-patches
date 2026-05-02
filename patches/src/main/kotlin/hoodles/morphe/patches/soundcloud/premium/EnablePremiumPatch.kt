package hoodles.morphe.patches.soundcloud.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import hoodles.morphe.patches.shared.misc.extension.sharedExtensionPatch
import hoodles.morphe.patches.soundcloud.shared.Constants

private val extensionPatch = sharedExtensionPatch(
    "soundcloud",
    activityOnCreateExtensionHook("/RootActivity;")
)

val enablePremiumPatch = bytecodePatch(
    name = "Enable SoundCloud Go+",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(extensionPatch)

    execute {
        // Force Features to be enabled
        FeatureConstructorFingerprint.method.addInstructions(1, """
            invoke-static {p1, p2}, Lhoodles/morphe/extension/soundcloud/feature/FeaturePatch;->getFeatureEnabled(Ljava/lang/String;Z)Z
            move-result p2
        """.trimIndent())

        // Override UserConsumerPlan
        UserConsumerPlanConstructorFingerprint.method.addInstructions(0, """
            const-string p1, "high_tier"
            const-string p5, "go-plus"
            const-string p6, "SoundCloud Go"
        """.trimIndent())

        // Override "pending_plan_downgrade" shared pref to prevent Offboarding screen
        GetDowngradeTierFingerprint.method.addInstructions(0, """
            sget-object v0, Lcom/soundcloud/android/configuration/plans/Tier;->HIGH:Lcom/soundcloud/android/configuration/plans/Tier;
            return-object v0
        """.trimIndent())

        // Always return UpsellType.None to prevent upgrade UI elements
        MapToPlanFingerprint.method.addInstructions(0, """
            sget-object v0, Lcom/soundcloud/android/upsell/UpsellType${'$'}None;->INSTANCE:Lcom/soundcloud/android/upsell/UpsellType${'$'}None;
            return-object v0
        """.trimIndent())

        // Hide persistent ads by patching both constructors
        AdPlacementConfigCtorFingerprint.matchAll().forEach { match ->
            val parameterOffset = if (match.method.parameterTypes.first() == "I") 1 else 0
            match.method.addInstructions(0,
                listOf(1, 2, 3).joinToString("\n") { "const/4 p${parameterOffset + it}, 0x0" }
            )
        }
    }

}