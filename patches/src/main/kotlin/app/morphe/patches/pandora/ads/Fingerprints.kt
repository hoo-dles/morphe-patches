package app.morphe.patches.pandora.ads

import app.morphe.patcher.Fingerprint

object GetIsAdSupportedFingerprint : Fingerprint (
    custom = { method, classDef ->
        method.name == "getIsAdSupported" && classDef.endsWith("UserData;")
    }
)

object RequestAudioAdFingerprint : Fingerprint (
    custom = { method, classDef ->
        method.name == "requestAudioAdFromAdSDK" && classDef.endsWith("ContentServiceOpsImpl;")
    }
)