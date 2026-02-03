package app.morphe.patches.dailypay.misc.gms

import app.morphe.patcher.Fingerprint

object MainActivityOnCreateFingerprint : Fingerprint (
    returnType = "V",
    parameters = listOf("Landroid/os/Bundle;"),
    custom = { method, classDef ->
        method.name == "onCreate" && classDef.endsWith("/MainActivity;")
    }
)