/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.goodreads.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import app.morphe.patches.all.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.all.misc.extension.sharedExtensionPatch
import app.morphe.util.returnEarly

internal val extensionPatch = sharedExtensionPatch(
    "goodreads",
    activityOnCreateExtensionHook("/RoutingActivity;")
)

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable ads",
    description = "Disables all ads contained within the UI."
) {
    compatibleWith(Compatibility(
        name = "Goodreads",
        packageName = "com.goodreads",
        appIconColor = 0xeae2d6,
        targets = listOf(AppTarget("2.75.0 Build 3"))
    ))

    dependsOn(extensionPatch)

    execute {
        AdspBagsAdSectionFactoryFingerprint.method.addInstructions(0, """
            const/4 p3, 0x1
        """.trimIndent())

        SectionListWithBannerAdFingerprint.method.addInstructions(0, """
            const/4 p2, 0x1
        """.trimIndent())

        SectionListMarkAdsHiddenFingerprint.method.addInstructions(0, """
            const/4 p1, 0x1
        """.trimIndent())

        SectionListMayShowAdFingerprint.method.returnEarly(false)

        val extensionPath = "Lhoodles/morphe/extension/goodreads/ads/DisableAdsPatch;"

        SectionListAdSectionFingerprint.method.let {
            it.addInstructionsWithLabels(
                0,
                """
                    invoke-static { p1 }, $extensionPath->shouldBlockSection(Ljava/lang/Object;)Z
                    move-result v0
                    if-nez v0, :end
                """.trimIndent(),
                ExternalLabel("end", it.instructions.last())
            )
        }

        ShouldInterceptRequestFingerprint.method.let {
            it.addInstructionsWithLabels(
                0,
                """
                    invoke-static { p2 }, $extensionPath->getBlockedRequest(Landroid/webkit/WebResourceRequest;)Landroid/webkit/WebResourceResponse;
                    move-result-object v0
                    if-eqz v0, :cont
                    return-object v0
                """.trimIndent(),
                ExternalLabel("cont", it.instructions.first())
            )
        }
    }
}