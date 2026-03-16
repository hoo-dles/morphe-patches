package app.morphe.patches.hellochinese.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.literal
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

object IsAuthInvalidFingerprint : Fingerprint (
    strings = listOf("AuthError", "10001", "decode")
)

object IsUserTypeEqual2Fingerprint : Fingerprint (
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
    filters = listOf(
        fieldAccess(name = "user_type"),
        literal(2, location = InstructionLocation.MatchAfterImmediately())
    ),
)

object IsUserTypeOver0Fingerprint : Fingerprint (
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
    filters = listOf(
        fieldAccess(name = "user_type"),
        opcode(Opcode.IF_LEZ, location = InstructionLocation.MatchAfterImmediately())
    ),
)
