/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.lightroom.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object IsLTPUActiveFingerprint : Fingerprint (
    classFingerprint = Fingerprint(
        strings = listOf("LTPU test is not enabled for this user, skipping initialization.")
    ),
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC, AccessFlags.FINAL),
    filters = listOf(
        opcode(Opcode.SGET_BOOLEAN, InstructionLocation.MatchFirst()),
        opcode(Opcode.RETURN, InstructionLocation.MatchAfterImmediately())
    )
)