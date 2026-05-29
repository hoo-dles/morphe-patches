package hoodles.morphe.patches.showly.premium

import app.morphe.patcher.Fingerprint

object GetVipFingerprint : Fingerprint(
    definingClass = "Lcom/michaldrabik/data_remote/trakt/model/User;",
    name = "getVip"
)

object IsPremiumFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("KEY_PREMIUM", "KEY_SHOW_PREMIUM_EXPIRED", "KEY_SHOW_PAYWALL")
    ),
    returnType = "Z"
)

object CheckEntitlementsFingerprint : Fingerprint(
    definingClass = "/QProductCenterManager;",
    name = "checkEntitlements"
)