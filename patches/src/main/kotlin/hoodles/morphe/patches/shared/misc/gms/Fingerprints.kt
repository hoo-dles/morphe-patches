/**
 * Original code is credited to Morphe:
 * https://github.com/MorpheApp/morphe-patches/blob/main/patches/src/main/kotlin/app/morphe/patches/shared/misc/gms/Fingerprints.kt
 *
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.gms

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object GooglePlayUtilityFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "I",
    parameters = listOf("L", "I"),
    strings = listOf(
        "This should never happen.",
        "MetadataValueReader",
        "com.google.android.gms",
    )
)

internal object IsGooglePlayServicesAvailableFingerprint : Fingerprint(
    name = "isGooglePlayServicesAvailable",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "I",
    parameters = listOf("Landroid/content/Context;", "I")
)

internal object ServiceCheckFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "V",
    parameters = listOf("L", "I"),
    strings = listOf("Google Play Services not available")
)

internal object GmsCoreSupportFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.STATIC),
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    custom = { method, classDef ->
        method.name == "getGmsCoreVendorGroupId" && classDef.type == EXTENSION_CLASS_DESCRIPTOR
    }
)

internal object OriginalPackageNameExtensionFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.STATIC),
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    custom = { methodDef, classDef ->
        methodDef.name == "getOriginalPackageName" && classDef.type == EXTENSION_CLASS_DESCRIPTOR
    }
)

internal fun getMainOnCreateFingerprint(activityClassType: String, targetBundleMethod: Boolean = true): Fingerprint {
    require(activityClassType.endsWith(';')) {
        "Class type must end with a semicolon: $activityClassType"
    }

    val fullClassType = activityClassType.startsWith('L')

    return Fingerprint(
        returnType = "V",
        parameters = if (targetBundleMethod) {
            listOf("Landroid/os/Bundle;")
        } else {
            listOf()
        },
        custom = { method, classDef ->
            method.name == "onCreate" &&
                    if (fullClassType) classDef.type == activityClassType
                    else classDef.type.endsWith(activityClassType)
        }
    )
}

internal object SetIsSystemProviderRequiredFingerprint : Fingerprint (
    filters = listOf(
        methodCall(
            name = "setIsSystemProviderRequired",
            definingClass = $$"Landroid/credentials/CredentialOption$Builder;",
            opcode = Opcode.INVOKE_VIRTUAL
        )
    )
)