package hoodles.morphe.patches.mimo.pro

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

object ProTierFingerprint : Fingerprint(
    name = "<clinit>",
    definingClass = "Lcom/getmimo/data/source/remote/iap/model/SubscriptionTier;",
    filters = listOf(
        string("pro"),
        string("Pro"),
        opcode(Opcode.SPUT_OBJECT)
    )
)

object SubscriptionStateCtorFingerprint : Fingerprint(
    name = "<init>",
    parameters = listOf(
        "Ljava/util/List;",
        "Lcom/revenuecat/purchases/EntitlementInfo;",
        "Lcom/getmimo/data/source/remote/iap/model/SubscriptionTier;",
        "Lcom/revenuecat/purchases/EntitlementInfo;"
    ),
    classFingerprint = Fingerprint(
        strings = listOf("SubscriptionState(entitlements=")
    )
)