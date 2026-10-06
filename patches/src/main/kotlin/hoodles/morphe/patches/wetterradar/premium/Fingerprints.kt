/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.wetterradar.premium

import app.morphe.patcher.Fingerprint

/**
 * The FusedAccessProvider's "isPro" combine lambda.
 *
 * [Ld95;] is a Function5 state-machine that reduces the four access inputs
 * (debug flag, membership login, Google Play purchase, login state) into the
 * single `Boolean` that populates the app-wide `isPro` flow. Returning `true`
 * here unlocks every feature gate and the ad-free experience.
 *
 * R8 names are version-bound: verify them against the target APK before bumping.
 */
object FusedAccessCombineFingerprint : Fingerprint(
    definingClass = "Ld95;",
    name = "invokeSuspend",
    parameters = listOf("Ljava/lang/Object;")
)

/**
 * The FusedAccessProvider's AccessLevel decision.
 *
 * [Lg95;.d] is the suspend function returning the access enum [Lo6;]
 * (b = SUBSCRIPTION, c = MEMBERSHIP, d = LOGIN, e = NONE). The UI switches on
 * `o6.ordinal()` to decide whether to show paywall/upgrade surfaces.
 */
object AccessLevelFingerprint : Fingerprint(
    definingClass = "Lg95;",
    name = "d",
    parameters = listOf("Lvo2;")
)
