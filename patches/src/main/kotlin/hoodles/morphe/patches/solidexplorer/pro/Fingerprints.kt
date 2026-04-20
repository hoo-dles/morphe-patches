package hoodles.morphe.patches.solidexplorer.pro

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object LicenseDetailsCtorFingerprint : Fingerprint(
    definingClass = "licensing/LicenseDetails;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR)
)