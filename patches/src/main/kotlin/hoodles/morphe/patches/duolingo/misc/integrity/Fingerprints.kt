package hoodles.morphe.patches.duolingo.misc.integrity

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object LoginStateFingerprint : Fingerprint(
    definingClass = "/LoginRequest\$EmailLoginRequest;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    returnType = "V"
)

object BasicLoginFingerprint : Fingerprint(
    definingClass = "/LoginFragmentViewModel;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Ljava/lang/String;", "Ljava/lang/String;"),
    returnType = "/LoginRequest\$EmailLoginRequest;"
)