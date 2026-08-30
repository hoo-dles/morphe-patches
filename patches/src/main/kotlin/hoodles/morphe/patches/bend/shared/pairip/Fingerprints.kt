package hoodles.morphe.patches.bend.shared.pairip

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object SentryNdkInitFingerprint : Fingerprint (
    definingClass = "Lio/sentry/android/ndk/SentryNdk;",
    name = "init"
)

object AdjustNSignFingerprint : Fingerprint (
    definingClass = "Lcom/adjust/sdk/sig/NativeLibHelper;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "[B"
)