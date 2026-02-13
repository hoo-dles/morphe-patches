package app.morphe.patches.protonvpn.delay

import app.morphe.patcher.Fingerprint

object GetLongDelayFingerprint : Fingerprint (
    custom = { method, _ -> method.name == "getChangeServerLongDelayInSeconds" }
)

object GetShortDelayFingerprint : Fingerprint (
    custom  = { method, _ -> method.name == "getChangeServerShortDelayInSeconds" }
)