package app.morphe.patches.fotmob.plus

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.checkCast
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.newInstance
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.fotmob.misc.extension.sharedExtensionPatch
import app.morphe.util.indexOfFirstInstructionReversed
import app.morphe.util.returnBoxedBooleanEarly
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

@Suppress("unused")
val enablePlusPatch = bytecodePatch(
    name = "Enable FobMob+",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith("com.mobilefootie.wc2010"("226.16092.20260302"))

    dependsOn(sharedExtensionPatch)

    execute {
        val subUtilClass = SubscriptionUtilClassFingerprint.classDef
        IsValidSubFingerprint.match(subUtilClass).method.returnEarly(true)
        HasActiveSubFingerprint.match(subUtilClass).method.returnBoxedBooleanEarly(value = true, force = true)

        val entitlementType = EntitlementFingerprint.classDef.type
        Fingerprint(filters = listOf(
            checkCast(entitlementType),
            newInstance(LifetimeEntitlementFingerprint.classDef.type)
        )).apply {
            val lifetimeNewInstanceIndex = instructionMatches.last().index
            val checkCastEntitlementIndex = method.indexOfFirstInstructionReversed(lifetimeNewInstanceIndex, Opcode.CHECK_CAST)
            val entitlementReg = method.getInstruction<OneRegisterInstruction>(checkCastEntitlementIndex).registerA

            // Create entitlement
            method.addInstructions(checkCastEntitlementIndex, """
                const-string v$entitlementReg, "$entitlementType"
                invoke-static {v$entitlementReg}, Lapp/morphe/extension/fotmob/plus/EnablePlusPatch;->createEntitlement(Ljava/lang/String;)Ljava/lang/Object;
                move-result-object v$entitlementReg
            """.trimIndent())
        }
    }
}