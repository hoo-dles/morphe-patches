package hoodles.morphe.patches.eggbun.misc.keyboard

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object KrKeyboardCtorFingerprint : Fingerprint(
    definingClass = "keyboard/KoreanKeyboardDelegateImpl;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR)
)