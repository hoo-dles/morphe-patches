package app.morphe.patches.protonvpn.splittunneling

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.Opcode

object SplitTunnelingSettingViewStateCtor : Fingerprint(
    strings = listOf("currentModeAppNames"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.MOVE_OBJECT,
        Opcode.MOVE_FROM16,
        Opcode.INVOKE_DIRECT_RANGE
    )
)

object ApplyRestrictionsFingerprint : Fingerprint(
    filters = listOf(methodCall(null, "getSplitTunneling")),
    custom = { method, _ -> method.name == "applyRestrictions"}
)