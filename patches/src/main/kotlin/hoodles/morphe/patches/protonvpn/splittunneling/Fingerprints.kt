/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.protonvpn.splittunneling

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.Opcode

object SplitTunnelingSettingViewStateCtor : Fingerprint(
    strings = listOf("currentModeAppNames"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.MOVE_OBJECT,
        Opcode.MOVE_FROM16,
        Opcode.INVOKE_DIRECT_RANGE
    )
)
