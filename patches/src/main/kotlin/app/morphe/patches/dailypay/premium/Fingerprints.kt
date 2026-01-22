package app.morphe.patches.dailypay.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

object WidgetConfigOnCreateFingerprint : Fingerprint (
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.IF_EQZ
    ),
    custom = { method, classDef ->
        classDef.endsWith("AppWidgetConfigureActivity;") && method.name == "onCreate"
    }
)