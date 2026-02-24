package app.morphe.patches.memegenerator.pro

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags

object IsFreeFingerprint : Fingerprint (
    parameters = listOf("Landroid/content/Context;"),
    returnType = "Ljava/lang/Boolean;",
    strings = listOf("free")
)

object IsCacheLicenseValid : Fingerprint (
    returnType = "Z",
    filters = listOf(
        methodCall(name = "currentTimeMillis"),
        literal(60000)
    )
)

object CheckSignatures1Fingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    parameters = listOf("Landroid/app/Activity;"),
    returnType = "Z",
    filters = listOf(
        methodCall(name = "toLowerCase")
    )
)

object CheckSignatures2Fingerprint : Fingerprint (
    parameters = listOf("Landroid/app/Activity;","L"),
    returnType = "Ljava/lang/Boolean;",
    filters = listOf(
        literal(3.0f),
        methodCall(name = "toLowerCase")
    )
)