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

// LoginFragmentViewModel is obfuscated since 6.104.x (e.g. j3o), so match by signature only.
// (String, String) -> EmailLoginRequest is unique in the app.
object BasicLoginFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Ljava/lang/String;", "Ljava/lang/String;"),
    returnType = "/LoginRequest\$EmailLoginRequest;"
)