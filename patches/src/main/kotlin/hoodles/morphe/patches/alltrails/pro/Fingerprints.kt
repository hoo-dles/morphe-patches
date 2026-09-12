/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.alltrails.pro

import app.morphe.patcher.Fingerprint

/** Gson User model (`Lgb60;` on 26.6.20, `Lw860;` on 26.7.40). */
object IsProFingerprint : Fingerprint(
    name = "isPro",
    returnType = "Z"
)

object GetSubscriptionTierFingerprint : Fingerprint(
    name = "getSubscriptionTier",
    returnType = "Ljava/lang/String;"
)

/**
 * 26.7.40 account delegate. Feature gates call subscription info built from
 * protobuf user data — bypass it and return Peak tier directly.
 */
internal object Cb60SubscriptionInfoFingerprint : Fingerprint(
    definingClass = "Lcb60;",
    name = "z",
    returnType = "Lab60\$a;"
)
