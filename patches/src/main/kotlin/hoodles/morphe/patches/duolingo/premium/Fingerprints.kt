package hoodles.morphe.patches.duolingo.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess

// Matches LoggedInState.toString()
object LoggedInStateFingerprint : Fingerprint(
    strings = listOf("LoggedIn(user=", ")")
)

// Matches User.toString()
object UserFingerprint : Fingerprint(
    strings = listOf("User(adsConfig=", ", id=", ", betaStatus=")
)

object UserIsPaidFieldUsageFingerprint : Fingerprint(
    definingClass = "Lcom/duolingo/sessionend/ads/",
    name = "test",
    parameters = listOf("Ljava/lang/Object;"),
    returnType = "Z",
    filters = listOf(
        fieldAccess(
            definingClass = "Lcom/duolingo/data/user/User;",
            type = "Z"
        )
    )
)

object UserHasGoldFieldUsageFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("VideoCallUserData(hasMax=", ", currentCourseId=")
    ),
    filters = listOf(
        fieldAccess(
            definingClass = "Lcom/duolingo/data/user/User;",
            type = "Z"
        )
    )
)