package app.morphe.patches.primevideo.ads

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object EnterServerInsertedAdBreakStateFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC),
    parameters = listOf("Lcom/amazon/avod/fsm/Trigger;"),
    returnType = "V",
    custom = { method, classDef ->
        method.name == "enter" && classDef.type == "Lcom/amazon/avod/media/ads/internal/state/ServerInsertedAdBreakState;"
    }
)

object DoTriggerFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PROTECTED),
    returnType = "V",
    custom = { method, classDef ->
        method.name == "doTrigger" && classDef.type == "Lcom/amazon/avod/fsm/StateBase;"
    }
)

object OnSeekPastUnwatchedAdFingerprint : Fingerprint (
    custom = {method, classDef ->
        method.name == "onSeekPastUnwatchedAd" && classDef.endsWith("SeekbarControllerImpl;")
    }
)

object OnSeekBehindUnwatchedAdFingerprint : Fingerprint (
    custom = {method, classDef ->
        method.name == "onSeekBehindUnwatchedAd" && classDef.endsWith("SeekbarControllerImpl;")
    }
)