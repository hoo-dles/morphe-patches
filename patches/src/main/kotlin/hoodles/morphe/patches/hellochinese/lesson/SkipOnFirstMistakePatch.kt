/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.hellochinese.lesson

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.patches.hellochinese.premium.enablePremiumPatch
import hoodles.morphe.util.requireArm64

@Suppress("unused")
val skipOnFirstMistakePatch = bytecodePatch(
    name = "Skip on first mistake",
    description = "Shows both skip and continue buttons from the first mistake."
) {
    compatibleWith(Compatibility(
        name = "HelloChinese",
        packageName = "com.hellochinese",
        appIconColor = 0xFFFFFF,
        targets = listOf(AppTarget("7.11.0"))
    ))

    availability(requireArm64())

    dependsOn(enablePremiumPatch)

    execute {
        // 1. Lower the wrong count threshold from 3 to 0 in V0().
        //    This makes CheckPanel.m = true from the very first mistake.
        val constIndex = SkipOnFirstMistakeFingerprint
            .instructionMatches[6]
            .index

        SkipOnFirstMistakeFingerprint.method.replaceInstruction(
            constIndex,
            "const/4 v1, 0x0"
        )

        // 2. In CheckPanel.h(), change mContinueBtnLayout visibility from GONE (0x8)
        //    to VISIBLE (0x0) so the continue button stays visible alongside skip.
        //    The fingerprint matches: IGET_OBJECT, CONST_16, INVOKE_VIRTUAL,
        //    IGET_OBJECT, CONST_4, INVOKE_VIRTUAL
        //    Index 1 is the CONST_16 for mContinueBtnLayout (0x8 → 0x0).
        val hMethod = CheckPanelHMethodFingerprint.method
        val visIndex = CheckPanelHMethodFingerprint
            .instructionMatches[1]
            .index

        hMethod.replaceInstruction(
            visIndex,
            "const/16 v1, 0x0"
        )
    }
}
