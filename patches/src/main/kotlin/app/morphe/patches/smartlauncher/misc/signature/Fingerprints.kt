package app.morphe.patches.smartlauncher.misc.signature

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall

object SignatureCheckFingerprint : Fingerprint (
    strings = listOf("Not genuine apk. This may not stop humans but may stop machines."),
    filters = listOf(
        methodCall(
            definingClass = "Ljava/lang/System;",
            name = "exit"
        )
    )
)