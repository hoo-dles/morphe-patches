package hoodles.morphe.patches.lyfta.premium

import app.morphe.patcher.Fingerprint

object GetSubscriptionTypeFingerprint : Fingerprint(
    definingClass = "Lcom/lyfta/util/helpers/Utils;",
    name = "getSubscriptionType"
)