package app.morphe.patches.busuu.premium

import app.morphe.patcher.Fingerprint

object IsPremiumFingerprint : Fingerprint (
    name = "isPremium"
)

// ApiUser
object ApiUserToStringFingerprint : Fingerprint (
    strings = listOf("ApiUser(legacyUid=")
)

// ApiUserAccess
object ApiUserAccessToStringFingerprint : Fingerprint (
    strings = listOf("ApiUserAccess(tier=")
)
object GetTierFingerprint : Fingerprint (
    name = "getTier"
)

// PremiumUser
object PremiumUserCtorFingerprint : Fingerprint (
    strings = listOf("legacyId", "name", "avatar", "accessTier")
)
object GetHasActiveSubscriptionFingerprint : Fingerprint (
    name = "getHasActiveSubscription"
)

