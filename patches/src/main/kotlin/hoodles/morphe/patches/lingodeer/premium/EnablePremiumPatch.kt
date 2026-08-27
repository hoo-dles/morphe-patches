/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.lingodeer.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c
import hoodles.morphe.patches.lingodeer.shared.stripPairipPatch
import hoodles.morphe.util.combine
import hoodles.morphe.util.requireArm64
import hoodles.morphe.util.requireRootMount
import java.time.ZonedDateTime


val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium (ROOT)",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Lingodeer",
        packageName = "com.lingodeer",
        appIconColor = 0x38d06c,
        targets = listOf(AppTarget("2.99.412"))
    ))

    availability(combine(requireRootMount, requireArm64()))

    dependsOn(stripPairipPatch)

    execute {
        BillingStatusCtorFingerprint.apply {
            val productIdReg = method.getInstruction<Instruction35c>(
                this.instructionMatches.first().index + 1).registerC
            val expiredDateMsReg = method.getInstruction<Instruction35c>(
                this.instructionMatches.last().index + 1).registerC

            val tenYearFuture = ZonedDateTime.now().plusYears(10).toInstant().toEpochMilli()

            method.addInstructions(0, """
                const-string v$productIdReg, "premium"
                const-string v$expiredDateMsReg, "$tenYearFuture"
            """.trimIndent())
        }
    }
}