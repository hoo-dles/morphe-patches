/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.protonvpn.lan

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.Field

fun getLanConnectionsSettingViewStateCtor(resField: Field) =
    Fingerprint(
        name = "<init>",
        filters = listOf(
            fieldAccess(reference = resField),
            opcode(Opcode.CONST_4),
            opcode(Opcode.MOVE_OBJECT),
            opcode(Opcode.MOVE),
            opcode(Opcode.INVOKE_DIRECT_RANGE)
        )
    )