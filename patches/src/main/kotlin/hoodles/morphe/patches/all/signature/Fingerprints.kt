/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.all.signature

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.Opcode

object GetPackageInfoFingerprint : Fingerprint (
    filters = listOf(
        methodCall(
            name = "getPackageInfo",
            definingClass = "Landroid/content/pm/PackageManager;",
            returnType = "Landroid/content/pm/PackageInfo;",
            opcode = Opcode.INVOKE_VIRTUAL
        )
    ),
    custom = { _, classDef ->
        !classDef.type.startsWith("Lhoodles/morphe/extension")
    }
)