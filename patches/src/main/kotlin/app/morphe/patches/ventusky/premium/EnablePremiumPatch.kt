package app.morphe.patches.ventusky.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Ventusky",
        packageName = "cz.ackee.ventusky",
        appIconColor = 0X4D71DE,
        targets = listOf(AppTarget("52.0"))
    ))

    execute {
        val premiumStaticField = PremiumCodeCtorFingerprint.instructionMatches.last()
            .getInstruction<ReferenceInstruction>()
            .getReference<FieldReference>()!!

        GetPlanStatusFingerprint.matchAll().forEach {
            it.method.addInstructions(0, """
                sget-object v0, $premiumStaticField
                return-object v0
            """.trimIndent())
        }
    }
}