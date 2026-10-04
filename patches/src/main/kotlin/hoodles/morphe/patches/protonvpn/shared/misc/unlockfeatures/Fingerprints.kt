package hoodles.morphe.patches.protonvpn.shared.misc.unlockfeatures

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal

object IsPlusMemberFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("VpnUser(userId=", ", subscribed=")
    ),
    parameters = emptyList(),
    returnType = "Z",
    filters = listOf(
        literal(2)
    )
)