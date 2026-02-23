package app.morphe.patches.sofascore.ads

import app.morphe.patcher.Fingerprint

const val CLASS_NAME = "UserAccount;"

object GetForceAdsFingerprint : Fingerprint(
    custom = { method, classDef -> classDef.endsWith(CLASS_NAME) && method.name == "getForceAds"}
)

object GetForceHideAdsFingerprint : Fingerprint(
    custom = { method, classDef -> classDef.endsWith(CLASS_NAME) && method.name == "getForceHideAds"}
)

object GetHasServerAdsFingerprint : Fingerprint(
    custom = { method, classDef -> classDef.endsWith(CLASS_NAME) && method.name == "getHasServerAds"}
)
