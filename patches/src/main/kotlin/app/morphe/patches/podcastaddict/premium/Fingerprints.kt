package app.morphe.patches.podcastaddict.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object HasPremiumFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    strings = listOf("pref_previousTimerUpdate")
)

object IsValidPackageSourceFingerprint : Fingerprint (
    strings = listOf("PAA", "NULL/NULL - false")
)

object IsValidSignatureFingerprint : Fingerprint (
    strings = listOf("Invalid Signature (",") - Donate: ")
)