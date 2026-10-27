/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.duolingo.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
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
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    parameters = listOf("Lcom/duolingo/data/user/User;", "L"),
    filters = listOf(
        methodCall(name = "getClass"),
        fieldAccess(definingClass = "Lcom/duolingo/data/user/User;")
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

fun getVideoCallTabCtaButtonStateFieldFingerprint(field: String) : Fingerprint {
    return Fingerprint(
        strings = listOf("VideoCallTabCtaButtonState(userHasMax="),
        filters = listOf(
            string(field),
            opcode(Opcode.IGET_BOOLEAN)
        )
    )
}

object EnergyConfigCtorFingerprint : Fingerprint(
    definingClass = "Lcom/duolingo/core/energy/models/EnergyConfig;",
    name = "<init>",
    parameters = listOf("I", "I", "J", "J", "J", "I", "J", "Ljava/lang/Integer;")
)