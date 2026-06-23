package hoodles.morphe.patches.xodo.misc.signature

import app.morphe.patcher.Fingerprint

object SignatureCheckFingerprint : Fingerprint(
    parameters = listOf("Lviewer/CompleteReaderMainActivity;"),
    strings = listOf("UTF_8")
)