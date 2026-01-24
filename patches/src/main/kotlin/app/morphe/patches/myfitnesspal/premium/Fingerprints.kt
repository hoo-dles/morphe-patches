package app.morphe.patches.myfitnesspal.premium

import app.morphe.patcher.Fingerprint

object GetPremiumPlusFingerprint : Fingerprint (
    custom = { method, classDef ->
        classDef.type == "Lcom/myfitnesspal/service/premium/data/SubscriptionPreferences;" &&
                method.name == "getPremiumPlusEnabled"
    }
)