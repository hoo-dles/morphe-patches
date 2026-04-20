package hoodles.morphe.patches.pydroid.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.patches.pydroid.misc.meta.includeOriginalMetadataPatch

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "PyDroid3",
        packageName = "ru.iiec.pydroid3",
        appIconColor = 0x3776AB,
        targets = listOf(AppTarget("8.3_arm64"))
    ))

    dependsOn(includeOriginalMetadataPatch)

    execute {
        SetIsPremiumFingerprint.method.addInstructions(0, """
            const/4 p1, 0x1
        """.trimIndent())
    }
}