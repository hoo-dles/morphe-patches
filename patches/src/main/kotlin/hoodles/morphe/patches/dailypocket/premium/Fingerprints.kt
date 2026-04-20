package hoodles.morphe.patches.dailypocket.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.Opcode

object WidgetConfigOnCreateFingerprint : Fingerprint(
    name = "OnCreate",
    definingClass = "AppWidgetConfigureActivity;",
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.IF_EQZ
    )
)