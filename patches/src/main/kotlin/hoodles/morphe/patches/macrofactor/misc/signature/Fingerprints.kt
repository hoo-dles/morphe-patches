package hoodles.morphe.patches.macrofactor.misc.signature

import app.morphe.patcher.Fingerprint

object GetSignatureFingerprint : Fingerprint(
    definingClass = "/AndroidUtilsLight;",
    name = "getPackageCertificateHashBytes"
)