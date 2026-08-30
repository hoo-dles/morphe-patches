/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.niagara.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.fix.changepackageinstaller.changePackageInstallerPatch
import hoodles.morphe.patches.niagara.misc.signature.spoofSignaturePatch
import hoodles.morphe.patches.niagara.shared.Constants

val enableProPatch = bytecodePatch(
    name = "Enable Niagara Pro",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(spoofSignaturePatch, changePackageInstallerPatch())

    execute {
        val match = ProStateClassFingerprint.matchAll().mapNotNull {
            // Fingerprint objects cache results, so we need a new one each time
            getProStateConstructorFingerprint().matchOrNull(it.originalClassDef)
        }.single()

        match.method.addInstructions(0, """
            const/4 p1, 0x1
            const/4 p2, 0x1
        """.trimIndent())
    }
}