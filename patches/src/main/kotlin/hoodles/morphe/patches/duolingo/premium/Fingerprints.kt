package hoodles.morphe.patches.duolingo.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

// Matches LoggedInState.toString()
object LoggedInStateFingerprint : Fingerprint(
    strings = listOf("LoggedIn(user=", ")")
)

// Matches User.toString()
object UserFingerprint : Fingerprint(
    strings = listOf("User(adsConfig=", ", id=", ", betaStatus=")
)

object UserIsPaidFieldUsageFingerprint : Fingerprint(
    parameters = listOf("Lcom/duolingo/data/user/User;", "Lcom/duolingo/data/home/CoursePathInfo;", "Z", "Lcom/duolingo/core/experiments/ExperimentsRepository\$TreatmentRecord;"),
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
        strings = listOf("MaxHooksUserData(isAdmin=")
    ),
    filters = listOf(
        fieldAccess(
            definingClass = "Lcom/duolingo/data/user/User;",
            type = "Z"
        )
    )
)

fun getFeatureFingerprint(feature: String) = Fingerprint(
    filters = listOf(
        fieldAccess(
            name = feature,
            definingClass = "Lcom/duolingo/core/subscription/models/SubscriptionFeatures;"
        ),
        methodCall(
            name = "contains",
            location = InstructionLocation.MatchAfterImmediately()
        ),
        opcode(Opcode.MOVE_RESULT, InstructionLocation.MatchAfterImmediately())
    )
)

object VideoCallTabCtaButtonStateToStringFingerprint : Fingerprint(
    strings = listOf("VideoCallTabCtaButtonState(userHasMax="),
    filters = listOf(
        string(", isEligibleForSecondaryUpsell="),
        opcode(Opcode.IGET_BOOLEAN)
    )
)