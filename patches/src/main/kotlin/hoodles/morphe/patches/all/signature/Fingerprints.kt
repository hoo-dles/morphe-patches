/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.all.signature

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string

object SignatureSpoofApplicationCtorFingerprint : Fingerprint (
    definingClass = Constants.SPOOF_CLASS_SMALI_NAME,
    name = "<clinit>",
    filters = listOf(
        string("PACKAGE_NAME_PLACEHOLDER"),
        string("SIGNATURE_PLACEHOLDER")
    )
)