package hoodles.morphe.patches.duolingo.misc.googlelogin

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

/**
 * androidx.credentials `CredentialProviderFrameworkImpl` (obfuscated, e.g. `m79` in 6.104.5).
 * Matched through the log string in `onClearCredential`, which survives R8.
 */
internal object CredentialProviderFrameworkImplFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    strings = listOf("In CredentialProviderFrameworkImpl onClearCredential")
)

/**
 * `CredentialProviderPlayServicesImpl.onGetCredential`:
 *
 * ```
 * if (isAvailableOnDevice(PRE_U_MIN_GMS_APK_VERSION /* 252400000 */)) GetCredentialController(...)
 * else if (isGetSignInIntentRequest(...)) CredentialProviderGetSignInIntentController(...)
 * else CredentialProviderBeginSignInController(...)
 * ```
 */
internal object PlayServicesGetCredentialFingerprint : Fingerprint(
    definingClass = "Landroidx/credentials/playservices/CredentialProviderPlayServicesImpl;",
    name = "onGetCredential",
    returnType = "V",
    filters = listOf(
        literal(252400000),
        methodCall(name = "isAvailableOnDevice"),
        opcode(Opcode.MOVE_RESULT)
    )
)
