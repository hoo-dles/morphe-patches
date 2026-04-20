package hoodles.morphe.patches.myfitnesspal.premium

import app.morphe.patcher.Fingerprint

object GetPremiumPlusFingerprint : Fingerprint(
    name = "getPremiumPlusEnabled",
    definingClass = "Lcom/myfitnesspal/service/premium/data/SubscriptionPreferences;"
)