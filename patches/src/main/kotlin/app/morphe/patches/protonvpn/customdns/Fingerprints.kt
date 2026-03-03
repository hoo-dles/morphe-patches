package app.morphe.patches.protonvpn.customdns

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.Opcode

object CustomDNSSettingViewStateCtor : Fingerprint(
    strings = listOf("customDns"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.CONST_4,
        Opcode.CONST_4,
        Opcode.MOVE_OBJECT,
        Opcode.MOVE,
        Opcode.INVOKE_DIRECT_RANGE
    )
)

object ApplyRestrictionsFingerprint : Fingerprint(
    name = "applyRestrictions",
    definingClass = "/BaseApplyEffectiveUserSettings;",
    filters = listOf(
        methodCall(name = "getCustomDns")
    ),
)