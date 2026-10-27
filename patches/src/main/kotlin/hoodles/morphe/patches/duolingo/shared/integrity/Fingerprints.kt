/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.duolingo.shared.integrity

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object LoginStateFingerprint : Fingerprint(
    definingClass = "/LoginRequest\$EmailLoginRequest;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    returnType = "V"
)

// Matches LoginFragmentViewModel method building the email login request (class name is obfuscated)
object BasicLoginFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Ljava/lang/String;", "Ljava/lang/String;"),
    returnType = "Lcom/duolingo/data/signuplogin/LoginRequest\$EmailLoginRequest;"
)