package app.morphe.patches.eggbun.premium

import app.morphe.patcher.Fingerprint

const val ACCOUNT_CLASS = "Lcom/eggbun/chat2learn/primer/model/Account;"

object IsLifetimePremiumFingerprint : Fingerprint (
    custom = { method, classDef ->
        method.name == "isLifetimePremium" && classDef.type == ACCOUNT_CLASS
    }
)

object GetExpiredFingerprint : Fingerprint (
    custom = { method, classDef ->
        method.name == "getExpired" && classDef.type == ACCOUNT_CLASS
    }
)

object GetLockedLessonRefFingerprint : Fingerprint (
    custom = { method, classDef ->
        method.name == "getLocked" && classDef.type == "Lcom/eggbun/chat2learn/primer/model/ContentsRef\$LessonRef;"
    }
)

object GetLockedLessonDetailsFingerprint : Fingerprint (
    custom = { method, classDef ->
        method.name == "getLocked" && classDef.type == "Lcom/eggbun/chat2learn/primer/network/dto/LessonDetailsState;"
    }
)