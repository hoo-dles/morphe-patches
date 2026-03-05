package app.morphe.patches.nomone.misc.tamper

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.literal
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object IsValidSignatureFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    parameters = listOf("Landroid/content/Context;"),
    returnType = "Z",
    filters = listOf (
        fieldAccess(
            opcode = Opcode.SGET_OBJECT,
            definingClass = "Lcom/google/android/gms/internal/play_billing/" ,
        ),
        literal(0, location = InstructionLocation.MatchAfterImmediately())
    )
)

object IsCrackedFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    parameters = listOf("Landroid/content/Context;"),
    returnType = "Z",
    strings = listOf("Border ID", "GPA.")
)