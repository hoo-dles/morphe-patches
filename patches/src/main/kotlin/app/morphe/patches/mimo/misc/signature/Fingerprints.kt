package app.morphe.patches.mimo.misc.signature

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object SignatureBytesToStringFingerprint : Fingerprint (
    parameters = listOf("[B", "Z"),
    returnType = "Ljava/lang/String;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    definingClass = "Lcom/google/android/gms/common/util/"
)

object SignatureFromPackageFingerprint : Fingerprint (
    parameters = listOf("Landroid/content/pm/PackageManager;", "Ljava/lang/String;"),
    returnType = "Ljava/lang/String;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC)
)