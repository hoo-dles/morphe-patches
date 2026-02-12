package app.morphe.patches.solidexplorer.pro

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object LicenseDetailsCtorFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    custom = { _, classDef -> classDef.endsWith("licensing/LicenseDetails;")}
)