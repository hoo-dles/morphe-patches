package hoodles.morphe.patches.macrofactor.premium

import app.morphe.patcher.Fingerprint

object BuildCustomerInfoFingerprint : Fingerprint(
    name = "buildCustomerInfo",
    definingClass = "/CustomerInfoFactory;",
    strings = listOf("subscriptions")
)