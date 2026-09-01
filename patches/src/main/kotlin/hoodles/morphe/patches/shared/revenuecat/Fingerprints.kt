package hoodles.morphe.patches.shared.revenuecat

import app.morphe.patcher.Fingerprint

object EntitlementInfosCtorFingerprint : Fingerprint (
    definingClass = "Lcom/revenuecat/purchases/EntitlementInfos;",
    name = "<init>",
    parameters = listOf("Ljava/util/Map;", "Lcom/revenuecat/purchases/VerificationResult;")
)

object CustomerInfoCtorFingerprint : Fingerprint (
    definingClass = "Lcom/revenuecat/purchases/CustomerInfo;",
    name = "<init>",
    parameters = listOf(
        "Lcom/revenuecat/purchases/EntitlementInfos;",
        "Ljava/util/Map;",
        "Ljava/util/Map;",
        "Ljava/util/Date;",
        "I",
        "Ljava/util/Date;",
        "Ljava/lang/String;",
        "Landroid/net/Uri;",
        "Ljava/util/Date;",
        "Lorg/json/JSONObject;",
        "Lcom/revenuecat/purchases/CustomerInfoOriginalSource;",
        "Z"
    )
)