package app.morphe.patches.primevideo.ads

import app.morphe.patcher.Fingerprint
import app.morphe.util.hasFlag
import com.android.tools.smali.dexlib2.AccessFlags

object EnterServerInsertedAdBreakStateFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC),
    parameters = listOf("Lcom/amazon/avod/fsm/Trigger;"),
    returnType = "V",
    name = "enter",
    definingClass = "Lcom/amazon/avod/media/ads/internal/state/ServerInsertedAdBreakState;"
)

object DoTriggerFingerprint : Fingerprint (
    returnType = "V",
    name = "doTrigger",
    definingClass = "Lcom/amazon/avod/fsm/StateBase;"
)

// The owning class has changed for these methods in v3.0.443. Just look for method names in non-abstract class.
object OnSeekPastUnwatchedAdFingerprint : Fingerprint (
    name = "onSeekPastUnwatchedAd",
    custom = {_, classDef -> !classDef.hasFlag(AccessFlags.ABSTRACT) }
)
object OnSeekBehindUnwatchedAdFingerprint : Fingerprint (
    name = "onSeekBehindUnwatchedAd",
    custom = {_, classDef -> !classDef.hasFlag(AccessFlags.ABSTRACT) }
)