package app.morphe.patches.teuida.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import com.android.tools.smali.dexlib2.AccessFlags

object PremiumGetterFingerprint : Fingerprint (
    definingClass = "Lnet/teuida/teuida/modelKt/MeData;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Ljava/lang/Boolean;",
    filters = listOf(
        fieldAccess(name = "premium")
    )
)
