package app.morphe.patches.mimo.premium

import app.morphe.patcher.Fingerprint

object IsActiveNowFingerprint : Fingerprint(
    definingClass = "/Subscription;",
    name = "isActiveNow"
)

object GetTypeFingerprint : Fingerprint(
    definingClass = "/Subscription;",
    name = "getType"
)