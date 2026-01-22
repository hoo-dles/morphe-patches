package app.morphe.patches.crunchyroll.premium

import app.morphe.patcher.Fingerprint

object HasPremiumBenefitFingerprint : Fingerprint (
    custom = { method, classDef ->
        method.name == "hasPremiumBenefit" && classDef.endsWith("BenefitKt;")
    }
)