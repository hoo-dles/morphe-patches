package hoodles.morphe.patches.xodo.misc.signature

import app.morphe.patcher.Fingerprint

object SignatureCheckFingerprint : Fingerprint(
    strings = listOf("completeReaderMainActivity", "completeReaderMainActivity.application")
)