package app.morphe.patches.pandora.misc

import app.morphe.patcher.Fingerprint

object SkipLimitBehaviorFingerprint : Fingerprint (
    custom = { method, classDef ->
        method.name == "getSkipLimitBehavior" && classDef.endsWith("UserData;")
    }
)