package app.morphe.patches.smartlauncher.pro

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object PurchaseItemsCtor : Fingerprint (
    accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    filters = listOf(
        string("lifetime"),
        opcode(Opcode.INVOKE_DIRECT),
        opcode(Opcode.SPUT_OBJECT),
        opcode(Opcode.NEW_INSTANCE)
    )
)

object PurchasableItemSetFingerprint : Fingerprint (
    strings = listOf("ginlemon.action.hasPremiumAccessChanged")
)

object PurchasableItemGetFingerprint : Fingerprint (
    parameters = listOf(),
    returnType = "Z"
)

object GetAppFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    parameters = listOf(),
    returnType = "Lginlemon/flower/App;"
)