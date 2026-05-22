package hoodles.morphe.patches.memegenerator.pro

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object IsFreeFingerprint : Fingerprint(
    parameters = listOf("Landroid/content/Context;"),
    returnType = "Ljava/lang/Boolean;",
    strings = listOf("free")
)

object IsCacheLicenseValidFingerprint : Fingerprint(
    returnType = "Z",
    filters = listOf(
        methodCall(name = "currentTimeMillis"),
        literal(256),
        literal(60000)
    )
)

object GetSignatureFingerprint : Fingerprint(
    parameters = listOf("[B"),
    returnType = "Ljava/lang/String;",
    strings = listOf("SHA-1")
)

object LicenseCheckFingerprint : Fingerprint(
    parameters = listOf("Lcom/zombodroid/memegen6source/SplashActivity;"),
    filters = listOf(
        opcode(Opcode.CMP_LONG),
        literal(1),
        opcode(Opcode.IF_GEZ)
    )
)