package hoodles.morphe.patches.lingodeer.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string

object BillingStatusCtorFingerprint : Fingerprint (
    definingClass = "Lcom/lingodeer/data/model/BillingStatus;",
    name = "<init>",
    filters = listOf(
        string("productId"),
        string("expiredDateMs")
    )
)