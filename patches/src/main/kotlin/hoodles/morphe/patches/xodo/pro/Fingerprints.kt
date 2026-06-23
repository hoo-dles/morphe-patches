package hoodles.morphe.patches.xodo.pro

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

object IsProFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("Monthly", "Yearly"),
        parameters = listOf("I", "Ljava/lang/String;")
    ),
    parameters = listOf(),
    returnType = "Z",
    filters = listOf(
        opcode(Opcode.INVOKE_VIRTUAL)
    )
)