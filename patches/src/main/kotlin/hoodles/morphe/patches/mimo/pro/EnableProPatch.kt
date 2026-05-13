package hoodles.morphe.patches.mimo.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import hoodles.morphe.patches.mimo.misc.signature.spoofSignatureHeaderPatch

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Mimo",
        packageName = "com.getmimo",
        appIconColor = 0x7E4BDE,
        targets = listOf(AppTarget("9.11"))
    ))

    dependsOn(spoofSignatureHeaderPatch)

    execute {
        val proTierField = ProTierFingerprint.instructionMatches.last()
            .getInstruction<ReferenceInstruction>().getReference<FieldReference>()

        SubscriptionStateCtorFingerprint.method.addInstructions(0, """
            sget-object p3, $proTierField
        """.trimIndent())
    }
}