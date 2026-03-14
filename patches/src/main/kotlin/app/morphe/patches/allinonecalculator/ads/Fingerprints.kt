package app.morphe.patches.allinonecalculator.ads

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.Opcode

object RegisterPluginsFingerprint : Fingerprint(
    definingClass = "Lio/flutter/plugins/GeneratedPluginRegistrant;",
    name = "registerWith",
    filters = listOf(
        methodCall(
            opcode = Opcode.INVOKE_DIRECT,
            definingClass = "Lio/flutter/plugins/googlemobileads/GoogleMobileAdsPlugin;"
        ),
        methodCall(
            opcode = Opcode.INVOKE_INTERFACE,
            name = "add",
            location = InstructionLocation.MatchAfterImmediately()
        )
    )
)