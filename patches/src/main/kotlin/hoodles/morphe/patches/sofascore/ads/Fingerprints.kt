package hoodles.morphe.patches.sofascore.ads

import app.morphe.patcher.Fingerprint

const val CLASS_NAME = "UserAccount;"

object GetForceAdsFingerprint : Fingerprint(
    name = "getForceAds",
    definingClass = CLASS_NAME
)

object GetForceHideAdsFingerprint : Fingerprint(
    name = "getForceHideAds",
    definingClass = CLASS_NAME
)

object GetHasServerAdsFingerprint : Fingerprint(
    name = "getHasServerAds",
    definingClass = CLASS_NAME
)
