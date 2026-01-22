package app.morphe.patches.avocards.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object GetPremiumUserFingerprint : Fingerprint(
    custom = { method, classDef ->
        method.name == "getPremium" && classDef.type == "Lcom/avocards/data/entity/UserEntity;"
    }
)

object UserEntityCtorFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    custom = { _, classDef ->
        classDef.type == "Lcom/avocards/data/entity/UserEntity;"
    }
)

object GetPremiumBaseFingerprint : Fingerprint(
    custom = { method, classDef ->
        method.name == "getPremium" && classDef.type == "Lcom/avocards/data/entity/BaseEntity;"
    }
)

object IsPremiumFingerprint : Fingerprint (
    custom = { method, classDef ->
        method.name == "isPremium" && classDef.type == "Lcom/avocards/data/manager/UserManager;"
    }
)