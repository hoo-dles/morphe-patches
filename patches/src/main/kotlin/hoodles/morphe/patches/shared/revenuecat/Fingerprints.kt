package hoodles.morphe.patches.shared.revenuecat

import app.morphe.patcher.Fingerprint

object EntitlementInfosCtorFingerprint : Fingerprint (
    definingClass = "Lcom/revenuecat/purchases/EntitlementInfos;",
    name = "<init>",
    parameters = listOf("Ljava/util/Map;", "Lcom/revenuecat/purchases/VerificationResult;")
)