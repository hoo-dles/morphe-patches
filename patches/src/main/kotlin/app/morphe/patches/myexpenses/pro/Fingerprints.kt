package app.morphe.patches.myexpenses.pro

import app.morphe.patcher.Fingerprint

object SetLicenseStatusFingerprint : Fingerprint (
    parameters = listOf("Lorg/totschnig/myexpenses/util/licence/LicenceStatus;"),
    strings = listOf("null", "Licence")
)

object GetValidUntilFingerprint : Fingerprint (
    returnType = "J",
    strings = listOf("licence_valid_until")
)