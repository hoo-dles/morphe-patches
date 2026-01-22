package app.morphe.patches.webster.premium

import app.morphe.patcher.Fingerprint

internal const val SUBSCRIPTION_CLASS = "Lcom/merriamwebster/dictionary/bean/MWSubscription;"

object GetSubscriptionFingerprint : Fingerprint (
    strings = listOf("PREF_CURRENT_SUBSCRIPTION"),
    returnType = SUBSCRIPTION_CLASS
)
