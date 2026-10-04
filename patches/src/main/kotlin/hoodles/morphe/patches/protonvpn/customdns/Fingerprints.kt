/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.protonvpn.customdns

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
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
