package app.morphe.patches.guessthecountry.premium

import app.morphe.patcher.Fingerprint

object IsProductInCacheFingerprint : Fingerprint (
    custom = { method, classDef ->
        method.name == "isIapProductInFileCache" && classDef.endsWith("AppActivity;")
    }
)