package hoodles.morphe.patches.nova.prime

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

object SetPrimeFromPreferencesFingerprint : Fingerprint(
    strings = listOf("android.os.SystemProperties"),
    filters = listOf(
        string("1"),
        literal(0),
        methodCall(name = "getInt"),
        opcode(Opcode.MOVE_RESULT)
    )
)