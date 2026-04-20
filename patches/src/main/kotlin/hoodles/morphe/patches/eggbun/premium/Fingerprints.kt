package hoodles.morphe.patches.eggbun.premium

import app.morphe.patcher.Fingerprint

const val ACCOUNT_CLASS = "Lcom/eggbun/chat2learn/primer/model/Account;"

object IsLifetimePremiumFingerprint : Fingerprint(
    name = "isLifetimePremium",
    definingClass = ACCOUNT_CLASS
)

object GetExpiredFingerprint : Fingerprint(
    name = "getExpired",
    definingClass = ACCOUNT_CLASS
)

object GetLockedLessonRefFingerprint : Fingerprint(
    name = "getLocked",
    definingClass = "Lcom/eggbun/chat2learn/primer/model/ContentsRef\$LessonRef;"
)

object GetLockedLessonDetailsFingerprint : Fingerprint(
    name = "getLocked",
    definingClass = "Lcom/eggbun/chat2learn/primer/network/dto/LessonDetailsState;"
)