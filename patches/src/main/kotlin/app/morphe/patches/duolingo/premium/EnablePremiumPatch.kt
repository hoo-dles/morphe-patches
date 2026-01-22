package app.morphe.patches.duolingo.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringOption
import app.morphe.patches.duolingo.shared.Utils.fieldFromToString
import app.morphe.util.*
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

enum class PremiumVariant {
    SUPER,
    MAX
}

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
) {
    compatibleWith("com.duolingo")

    val premiumVariant by stringOption(
        key = "premiumVariant",
        default = PremiumVariant.SUPER.name,
        values = mapOf(
            "Duolingo Super" to PremiumVariant.SUPER.name,
            "Duolingo MAX" to PremiumVariant.MAX.name
        ),
        title = "Type",
        description = "Choose which type of premium Duolingo subscription to enable.",
        required = true,
    )

    execute {
        val optionIsMax = enumValueOf<PremiumVariant>(premiumVariant!!) == PremiumVariant.MAX

        val hasPlusField = UserFingerprint.classDef.fieldFromToString("hasPlus")
        val subscriberLevelField = UserFingerprint.classDef.fieldFromToString("subscriberLevel")
        val subscriberLevel = if (optionIsMax) "GOLD" else "PREMIUM"

        // These fields are calculated in constructor, but not serialized. So we have to find their
        // name and set it late.
        val isPaidField = UserIsPaidFieldUsageFingerprint.method.let {
            val isPaidIndex = it.indexOfFirstInstructionOrThrow(Opcode.IGET_BOOLEAN)
            it.getInstruction<ReferenceInstruction>(isPaidIndex).getReference<FieldReference>()!!
        }
        val hasGoldField = UserHasGoldFieldUsageFingerprint.method.let {
            val hasGoldIndex = it.indexOfFirstInstructionOrThrow(Opcode.IGET_BOOLEAN)
            it.getInstruction<ReferenceInstruction>(hasGoldIndex).getReference<FieldReference>()!!
        }

        // Remove final keyword on fields we want to patch.
        val fields = mutableSetOf(hasPlusField, subscriberLevelField, isPaidField, hasGoldField)
        fields.forEach { UserFingerprint.classDef.fieldByName(it.name).removeFlag(AccessFlags.FINAL) }

        // For patching user properties, we target the User object that is passed in to the
        // constructor of the LoggedIn class. This way we don't affect all instances of users
        // (eg. viewing a friend's profile).
        LoggedInStateFingerprint.classDef.constructor().apply {
            val userType = UserFingerprint.classDef.type
            val patchIndex = this.instructions.count() - 1

            val instrSb = StringBuilder()
            instrSb.appendLine(
                """
                const/4 v0, 0x1
                iput-boolean v0, p1, $userType->${isPaidField.name}:Z
                iput-boolean v0, p1, $userType->${hasPlusField.name}:Z
                """.trimIndent()
            );

            if (optionIsMax) {
                instrSb.appendLine(
                    "iput-boolean v0, p1, $userType->${hasGoldField.name}:Z"
                )
            }

            instrSb.appendLine(
                """
                sget-object v0, ${subscriberLevelField.type}->$subscriberLevel:${subscriberLevelField.type}
                iput-object v0, p1, $userType->${subscriberLevelField.name}:${subscriberLevelField.type}
                """.trimIndent()
            )

            // Single-parameter method means User will always be in p1.
            // Inserting right before method return, so we can clobber existing registers.
            this.addInstructions(
                patchIndex, instrSb.toString()
            )
        }
    }
}