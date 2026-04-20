package hoodles.morphe.patches.adguard.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

object GetPlusStateFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("Failed to get state from backend. Remaining retry count: ")
    ),
    parameters = listOf(),
    returnType = "L",
    filters = listOf(
        fieldAccess(type = "Lcom/adguard/android/storage/"),
        opcode(Opcode.IPUT_OBJECT)
    )
)

object PaidLicenseFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("PaidLicense(licenseKey=")
    ),
    name = "<init>"
)

object LifetimeDurationFingerprint : Fingerprint(
    name = "toString",
    strings = listOf("Lifetime")
)