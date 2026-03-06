package app.morphe.patches.nomone.trial

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object PurchaseInfoUsageFingerprint : Fingerprint (
    definingClass = "Lcom/nomone/giap/GIAP;",
    parameters = listOf("Ljava/lang/String;", "I"),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    filters = listOf(
        string("IAP"),
        opcode(Opcode.IPUT)
    )
)

object GetPurchaseInfoFingerprint : Fingerprint (
    definingClass = "Lcom/nomone/giap/GIAP;",
    parameters = listOf("Ljava/lang/String;"),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "L",
    filters = listOf(
        opcode(Opcode.RETURN_OBJECT)
    )
)