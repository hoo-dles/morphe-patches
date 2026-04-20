package hoodles.morphe.patches.ventusky.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object GetPlanStatusFingerprint : Fingerprint(
    strings = listOf("premium_mode", "premium")
)

object PremiumCodeCtorFingerprint : Fingerprint(
    definingClass = "/UserPremiumVersion;",
    accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    strings = listOf("FREE", "PREMIUM", "PREMIUM_PLUS"),
    filters = listOf(
        string("PREMIUM"),
        opcode(Opcode.SPUT_OBJECT)
    )
)