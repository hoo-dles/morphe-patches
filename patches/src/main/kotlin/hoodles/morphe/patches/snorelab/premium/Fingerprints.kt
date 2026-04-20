package hoodles.morphe.patches.snorelab.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object IsRemotePremiumActiveFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("sharedPrefs", "jsonBridge", "remotePremiumActive")
    ),
    returnType = "Z",
    parameters = listOf()
)

object IsPremiumDateValidFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
        strings = listOf("userDataSharedPrefs"),
        parameters = listOf("L")
    ),
    returnType = "Z",
    parameters = listOf("L")
)