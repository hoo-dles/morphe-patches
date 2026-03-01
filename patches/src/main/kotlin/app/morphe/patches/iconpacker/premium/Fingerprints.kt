package app.morphe.patches.iconpacker.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object PurchaseUiStateFactoryFingerprint : Fingerprint(
    strings = listOf("membershipSubscriptionState", "oneTimeMembershipState")
)

object PurchaseUiStateCtorFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    filters = listOf(
        literal(0)
    )
)

object ExportSetupFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf("L", "L", "L", "Z", "Landroid/net/Uri;", "L"),
    filters = listOf(
        opcode(Opcode.IPUT_BOOLEAN),
        literal(2)
    )
)