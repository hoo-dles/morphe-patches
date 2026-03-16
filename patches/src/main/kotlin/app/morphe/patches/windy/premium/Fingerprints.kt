package app.morphe.patches.windy.premium

import app.morphe.patcher.Fingerprint

object IsPremiumForWidgetFingerprint : Fingerprint(
    parameters = listOf(),
    returnType = "Z",
    strings = listOf("premium")
)