package hoodles.morphe.patches.pandora.ads

import app.morphe.patcher.Fingerprint

object GetIsAdSupportedFingerprint : Fingerprint(
    name = "getIsAdSupported",
    definingClass = "UserData;"
)

object RequestAudioAdFingerprint : Fingerprint(
    name = "requestAudioAdFromAdSDK",
    definingClass = "ContentServiceOpsImpl;"
)