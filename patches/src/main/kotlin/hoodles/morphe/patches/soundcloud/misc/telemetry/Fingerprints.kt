package hoodles.morphe.patches.soundcloud.misc.telemetry

import app.morphe.patcher.Fingerprint

object HandleMessageFingerprint : Fingerprint(
    definingClass = "/TrackingHandler;",
    name = "handleMessage"
)