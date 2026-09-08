/**
 * Original code is credited to Morphe:
 * https://github.com/MorpheApp/morphe-patches-https://github.com/MorpheApp/morphe-patches/blob/main/patches/src/main/kotlin/app/morphe/patches/all/misc/resources/AddResourcesPatch.ktlibrary/blob/main/patch-library/src/main/kotlin/app/morphe/patches/all/misc/resources/ResourceMappingPatch.kt
 *
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.extension

import app.morphe.patcher.Fingerprint
import app.morphe.patches.all.misc.extension.SHARED_UTILS_EXTENSION_CLASS
import com.android.tools.smali.dexlib2.AccessFlags

internal object MorpheUtilsPatchesVersionFingerprint : Fingerprint(
    definingClass = SHARED_UTILS_EXTENSION_CLASS,
    name = "getPatchesReleaseVersion",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
)