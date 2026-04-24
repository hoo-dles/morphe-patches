package hoodles.morphe.patches.protonvpn.lan

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

object LanConnectionsSettingViewStateCtor : Fingerprint(
    name = "<init>",
    filters = listOf(
        fieldAccess(name = "settings_advanced_allow_lan_description"),
        opcode(Opcode.CONST_4),
        opcode(Opcode.MOVE_OBJECT),
        opcode(Opcode.MOVE),
        opcode(Opcode.INVOKE_DIRECT_RANGE)
    )
)

object ApplyRestrictionsFingerprint : Fingerprint(
    name = "applyRestrictions",
    definingClass = "/BaseApplyEffectiveUserSettings;",
    filters = listOf(
        methodCall(name = "getLanConnections")
    ),
)