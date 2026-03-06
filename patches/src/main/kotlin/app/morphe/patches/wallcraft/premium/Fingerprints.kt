package app.morphe.patches.wallcraft.premium

import app.morphe.patcher.Fingerprint

object GetSubscriptionStateFingerprint : Fingerprint (
    name = "getSubscriptionStateByOptions",
    definingClass = "Lcom/wallpaperscraft/billing/core/SubscriptionManager;"
)