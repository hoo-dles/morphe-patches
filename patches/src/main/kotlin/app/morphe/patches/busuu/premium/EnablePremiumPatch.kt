package app.morphe.patches.busuu.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium"
) {
    compatibleWith("com.busuu.android.enc")

    execute {
        IsPremiumFingerprint.match(ApiUserToStringFingerprint.classDef)
            .method.returnEarly(true)

        GetTierFingerprint.match(ApiUserAccessToStringFingerprint.classDef)
            .method.returnEarly("standard")

        val premiumUserClass = PremiumUserCtorFingerprint.classDef
        GetHasActieSubscriptionFingerprint.method.returnEarly(true)
        IsPremiumFingerprint.method.returnEarly(true)
    }
}