package app.morphe.patches.soundcloud.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object FeatureConstructorFingerprint : Fingerprint(
    definingClass = "/Feature;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf(
        "Ljava/lang/String;",
        "Z",
        "Ljava/util/List;",
    )
)

object UserConsumerPlanConstructorFingerprint : Fingerprint(
    definingClass = "/UserConsumerPlan;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf(
        "Ljava/lang/String;",
        "Z",
        "Ljava/lang/String;",
        "Ljava/util/List;",
        "Ljava/lang/String;",
        "Ljava/lang/String;"
    )
)

object GetDowngradeTierFingerprint : Fingerprint(
    definingClass = "/ConfigurationSettingsStorage;",
    strings = listOf("pending_plan_downgrade"),
    returnType = "/Tier;"
)

object MapToPlanFingerprint : Fingerprint(
    definingClass = "/RemoteUpsellVisibilityController;",
    name = "mapToPlan"
)