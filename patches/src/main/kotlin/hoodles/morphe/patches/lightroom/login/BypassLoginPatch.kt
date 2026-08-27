/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.lightroom.login

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import hoodles.morphe.patches.lightroom.shared.Constants
import hoodles.morphe.patches.lightroom.shared.stripPairipPatch
import hoodles.morphe.util.requireArm64

@Suppress("unused")
val bypassLoginPatch = bytecodePatch(
    name = "Bypass login",
    description = "Allows app use without login. BUT breaks many features. Requirements: arm64-v8a, strict apk version",
    default = false
) {
    compatibleWith(Constants.COMPATIBILITY)

    availability(requireArm64())

    dependsOn(stripPairipPatch)

    execute {
        val (isAuthField, wasAuthField) = AccountInfoFingerprint.let {
            val instrCount = it.instructionMatches.count()
            val isAuth = it.instructionMatches[instrCount - 2].instruction.getReference<FieldReference>()!!
            val wasAuth = it.instructionMatches[instrCount - 1].instruction.getReference<FieldReference>()!!

            Pair(isAuth, wasAuth)
        }

        val accountInfoClass = AccountInfoFingerprint.classDef
        getBooleanGetterFingerprint(isAuthField).match(accountInfoClass)
            .method.returnEarly(true)

        getBooleanGetterFingerprint(wasAuthField).match(accountInfoClass)
            .method.returnEarly(true)

        AccountStatusFromSharedPrefsFingerprint.method.apply {
            val type = this.returnType
            addInstructions(0, """
                sget-object v0, $type->Created:$type
                return-object v0
            """.trimIndent())
        }
    }
}