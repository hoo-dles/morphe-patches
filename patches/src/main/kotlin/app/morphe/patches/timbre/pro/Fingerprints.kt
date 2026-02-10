package app.morphe.patches.timbre.pro

import app.morphe.patcher.Fingerprint

object IsUserAProFingerprint : Fingerprint (
    custom = { method, _ ->
        method.name == "access\$isUserAPro\$cp"
    }
)