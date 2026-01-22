package app.morphe.patches.duolingo.premium

import app.morphe.patcher.Fingerprint

// Matches LoggedInState.toString()
object LoggedInStateFingerprint : Fingerprint (
    strings = listOf("LoggedIn(user=", ")")
)

// Matches User.toString()
object UserFingerprint : Fingerprint (
    strings = listOf("User(adsConfig=", ", id=", ", betaStatus=")
)

// Some method that has to do with subscription trials
object UserIsPaidFieldUsageFingerprint : Fingerprint (
    parameters = listOf("L", "L"),
    returnType = "Z",
    strings = listOf("user", "onboardingState")
)

// Some method that has to do with checking if MAX is enabled
object UserHasGoldFieldUsageFingerprint : Fingerprint (
    parameters = listOf("L", "L", "L"),
    returnType = "L",
    strings = listOf(
        "maxFeaturesEnabled",
        "isEmaEnabledInCourse",
        "user"
    )
)