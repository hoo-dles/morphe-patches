package app.morphe.patches.busuu.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "Busuu",
        packageName = "com.busuu.android.enc",
        targets = listOf(AppTarget("32.30.0"))
    ))

    execute {
        IsPremiumFingerprint.match(ApiUserToStringFingerprint.classDef)
            .method.returnEarly(true)

        GetTierFingerprint.match(ApiUserAccessToStringFingerprint.classDef)
            .method.returnEarly("standard")

        GetHasActiveSubscriptionFingerprint.method.returnEarly(true)
        IsPremiumFingerprint.match(PremiumUserCtorFingerprint.classDef).method.returnEarly(true)
    }
}