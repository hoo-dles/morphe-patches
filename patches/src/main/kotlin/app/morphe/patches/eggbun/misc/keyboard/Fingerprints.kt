package app.morphe.patches.eggbun.misc.keyboard

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object KrKeyboardCtorFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    custom = { _, classDef -> classDef.endsWith("keyboard/KoreanKeyboardDelegateImpl;") }
)