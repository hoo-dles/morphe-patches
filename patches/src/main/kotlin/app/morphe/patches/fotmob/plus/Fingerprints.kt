package app.morphe.patches.fotmob.plus

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall

object SubscriptionUtilClassFingerprint : Fingerprint (
    strings = listOf("Error getting active offerings")
)

object IsValidSubFingerprint : Fingerprint (
    returnType = "Z",
    filters = listOf(
        fieldAccess(type = "Ljava/lang/Boolean;"),
        methodCall(
            name = "get",
            definingClass = "Lcom/fotmob/storage/sharedpreference/SharedPreferencesRepository;"
        )
    )
)

object HasActiveSubFingerprint : Fingerprint (
    strings = listOf("call to 'resume' before 'invoke' with coroutine"),
    filters = listOf(
        methodCall(
            name = "getActiveSubscriptions",
            definingClass = "Lcom/revenuecat/purchases/CustomerInfo;"
        )
    )
)

object LifetimeEntitlementFingerprint : Fingerprint (
    strings = listOf("LifeTimeEntitlement(entitlement=")
)

object EntitlementFingerprint : Fingerprint (
    strings = listOf("Entitlement(identifier=")
)