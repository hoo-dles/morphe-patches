/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.lightroom.login

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

object AccountStatusFromSharedPrefsFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    strings = listOf("THUser::AccountStatus", "created")
)

object AccountInfoFingerprint : Fingerprint (
    strings = listOf("Missing authentication - login attempt"),
    filters = listOf(
        string("Valid Authentication Token"),
        // isAuthenticated
        opcode(Opcode.IPUT_BOOLEAN),
        // wasAuthenticated
        opcode(Opcode.IPUT_BOOLEAN, InstructionLocation.MatchAfterImmediately())
    )
)

fun getBooleanGetterFingerprint(field: FieldReference) = Fingerprint (
    returnType = "Z",
    filters = listOf(
        fieldAccess(field),
        opcode(Opcode.RETURN, InstructionLocation.MatchAfterImmediately())
    )
)
