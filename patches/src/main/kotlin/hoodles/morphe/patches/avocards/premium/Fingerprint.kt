package hoodles.morphe.patches.avocards.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object GetPremiumUserFingerprint : Fingerprint(
    name = "getPremium",
    definingClass = "Lcom/avocards/data/entity/UserEntity;"
)

object UserEntityCtorFingerprint : Fingerprint(
    definingClass = "Lcom/avocards/data/entity/UserEntity;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR)
)

object GetPremiumBaseFingerprint : Fingerprint(
    name = "getPremium",
    definingClass = "Lcom/avocards/data/entity/BaseEntity;"
)

object IsPremiumFingerprint : Fingerprint(
    name = "isPremium",
    definingClass = "Lcom/avocards/data/manager/UserManager;"
)