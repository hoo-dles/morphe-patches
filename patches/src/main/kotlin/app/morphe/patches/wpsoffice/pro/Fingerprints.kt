package app.morphe.patches.wpsoffice.pro

import app.morphe.patcher.Fingerprint

object HasPrivilegeFingerprint : Fingerprint(
    parameters = listOf("Ljava/lang/String;"),
    returnType = "Z",
    strings = listOf("ads_free_cn", "ads_free_i18n")
)
