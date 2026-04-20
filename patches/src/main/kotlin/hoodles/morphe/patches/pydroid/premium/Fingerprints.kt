package hoodles.morphe.patches.pydroid.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object SetIsPremiumFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("extra_text_to_save", "extra_default_file_name")
    ),
    accessFlags = listOf(AccessFlags.PRIVATE),
    parameters = listOf("Z"),
    returnType = "V"
)