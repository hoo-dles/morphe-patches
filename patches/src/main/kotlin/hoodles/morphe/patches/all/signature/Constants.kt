/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.all.signature

import com.android.tools.smali.dexlib2.analysis.reflection.util.ReflectionUtils

object Constants {
    const val SPOOF_CLASS_JAVA_NAME = "hoodles.morphe.extension.signature.SignatureSpoofApplication"
    val SPOOF_CLASS_SMALI_NAME: String = ReflectionUtils.javaToDexName(SPOOF_CLASS_JAVA_NAME)
}