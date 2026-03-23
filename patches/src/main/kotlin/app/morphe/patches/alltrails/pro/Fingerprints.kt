package app.morphe.patches.alltrails.pro

import app.morphe.patcher.Fingerprint

object IsProFingerprint : Fingerprint(
    name = "isPro",
    returnType = "Z"
)

object GetSubscriptionTierFingerprint : Fingerprint(
    name = "getSubscriptionTier",
    returnType = "Ljava/lang/String;"
)