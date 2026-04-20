package hoodles.morphe.patches.protonvpn.delay

import app.morphe.patcher.Fingerprint

object GetLongDelayFingerprint : Fingerprint(
    name = "getChangeServerLongDelayInSeconds"
)

object GetShortDelayFingerprint : Fingerprint(
    name = "getChangeServerShortDelayInSeconds"
)