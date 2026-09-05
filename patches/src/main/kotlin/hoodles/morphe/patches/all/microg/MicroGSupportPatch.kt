/**
 * Original code is credited to Morphe:
 * https://github.com/MorpheApp/morphe-patches/blob/main/patches/src/main/kotlin/app/morphe/patches/shared/misc/gms/GmsCoreSupportPatch.kt
 *
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.all.microg

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.extension.ExtensionHook
import app.morphe.util.findMutableMethodOf
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableStringReference
import hoodles.morphe.patches.shared.misc.extension.sharedExtensionPatch
import hoodles.morphe.patches.shared.misc.resources.addAppResources
import hoodles.morphe.patches.shared.misc.resources.addResourcesPatch

internal const val EXTENSION_CLASS_DESCRIPTOR = "Lhoodles/morphe/extension/microg/MicroGSupport;"
internal const val GMS_CORE_VENDOR_GROUP_ID = "app.revanced"

private val microGExtensionPatch = sharedExtensionPatch(
    "common/microg",
    { ExtensionHook(MicroGMetadata.onCreateFingerprint) },
    listOf(microGMetadataPatch)
)

@Suppress("unused")
val microGSupportPatch = bytecodePatch(
    name = "MicroG integration",
    description = "Allows the app to work without root by using MicroG instead of Google Play Services.",
    default = false
) {
//    val newPackageName by stringOption(
//        key = "newPackageName",
//        title = "New package name",
//        description = "If set, the app's package name will be changed to this value (usually not necessary).",
//    )

    dependsOn(
        microGMetadataPatch,
        addResourcesPatch,
        gmsCoreSupportResourcePatch,
        microGExtensionPatch
    )

    execute {
        fun transformStringReferences(transform: (str: String) -> String?) = classDefForEach {
            val mutableClass by lazy {
                mutableClassDefBy(it)
            }

            it.methods.forEach classLoop@{ method ->
                val implementation = method.implementation ?: return@classLoop

                val mutableMethod by lazy {
                    mutableClass.findMutableMethodOf(method)
                }

                implementation.instructions.forEachIndexed { index, instruction ->
                    val string = ((instruction as? Instruction21c)?.reference as? StringReference)?.string
                        ?: return@forEachIndexed

                    // Apply transformation.
                    val transformedString = transform(string) ?: return@forEachIndexed

                    mutableMethod.replaceInstruction(
                        index,
                        BuilderInstruction21c(
                            Opcode.CONST_STRING,
                            instruction.registerA,
                            ImmutableStringReference(transformedString),
                        ),
                    )
                }
            }
        }

        // region Collection of transformations that are applied to all strings.

        fun commonTransform(referencedString: String): String? = when (referencedString) {
            "com.google",
            "com.google.android.gms",
            in Constants.PERMISSIONS,
            in Constants.ACTIONS,
            in Constants.AUTHORITIES,
            in Constants.CAPABILITIES,
                -> referencedString.replace("com.google", GMS_CORE_VENDOR_GROUP_ID)

            // No vendor prefix for whatever reason...
            "subscribedfeeds" -> "$GMS_CORE_VENDOR_GROUP_ID.subscribedfeeds"
            else -> null
        }

        fun contentUrisTransform(str: String): String? {
            // only when content:// uri
            if (str.startsWith("content://")) {
                // check if matches any authority
                for (authority in Constants.AUTHORITIES) {
                    val uriPrefix = "content://$authority"
                    if (str.startsWith(uriPrefix)) {
                        return str.replace(
                            uriPrefix,
                            "content://${authority.replace("com.google", GMS_CORE_VENDOR_GROUP_ID)}",
                        )
                    }
                }

                // gms also has a 'subscribedfeeds' authority, check for that one too
                val subFeedsUriPrefix = "content://subscribedfeeds"
                if (str.startsWith(subFeedsUriPrefix)) {
                    return str.replace(subFeedsUriPrefix, "content://$GMS_CORE_VENDOR_GROUP_ID.subscribedfeeds")
                }
            }

            return null
        }

        fun packageNameTransform(fromPackageName: String, toPackageName: String): (String) -> String? = { string ->
            when (string) {
                "$fromPackageName.SuggestionProvider",
                "$fromPackageName.fileprovider",
                    -> string.replace(fromPackageName, toPackageName)

                else -> null
            }
        }

        addAppResources("gms")

        // Transform all strings using all provided transforms, first match wins.
        val transformations = mutableListOf(
            ::commonTransform,
            ::contentUrisTransform,
        )
//        if (changePackageName) {
//            val packageName = setOrGetFallbackPackageName(newPackageName!!)
//            transformations.add(packageNameTransform(fromPackageName, packageName) as KFunction1<String, String?>)
//        }

        transformStringReferences transform@{ string ->
            transformations.forEach { transform ->
                transform(string)?.let { transformedString -> return@transform transformedString }
            }

            return@transform null
        }

        ServiceCheckFingerprint.methodOrNull?.returnEarly()

        // Return status code 0 for play service availability checks.
        listOf(
            IsGooglePlayServicesAvailableFingerprint,
            GooglePlayUtilityFingerprint,
            IsGooglePlayServicesAvailableLightFingerprint
        ).forEach {
            it.methodOrNull?.apply { returnEarly(0) }
        }

        // Set original and patched package names for extension to use.
//        OriginalPackageNameExtensionFingerprint.method.returnEarly(fromPackageName ?: packageMetadata.packageName)

        // Verify GmsCore is installed and whitelisted for power optimizations and background usage.
        MicroGMetadata.onCreateFingerprint.method.apply {
            addInstruction(
                0,
                "invoke-static/range { p0 .. p0 }, $EXTENSION_CLASS_DESCRIPTOR->" +
                        "checkGmsCore(Landroid/app/Activity;)V"
            )
        }

        // Change the vendor of GmsCore in the extension.
        GmsCoreSupportFingerprint.method.returnEarly(GMS_CORE_VENDOR_GROUP_ID)

        // Force isSystemProviderRequired = false for all CredentialOption.
        // TODO: we might need to hook `CreateCredentialRequest` as well
        SetIsSystemProviderRequiredFingerprint.matchAllOrNull()?.forEach { match ->
            val setIndex = match.instructionMatches.first().index
            val sysReqReg = match.method.getInstruction<Instruction35c>(setIndex).registerD

            match.method.addInstruction(setIndex, "const/4 v$sysReqReg, 0x0")
        }
    }
}