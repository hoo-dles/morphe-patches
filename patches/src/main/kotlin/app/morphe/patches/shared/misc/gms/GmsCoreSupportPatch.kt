package app.morphe.patches.shared.misc.gms

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.BytecodePatchBuilder
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.Patch
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.packagename.changePackageNamePatch
import app.morphe.patches.all.misc.packagename.setOrGetFallbackPackageName
import app.morphe.patches.all.misc.resources.addAppResources
import app.morphe.patches.all.misc.resources.addResourcesPatch
import app.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.shared.misc.extension.sharedExtensionPatch
import app.morphe.util.findMutableMethodOf
import app.morphe.util.getReference
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableStringReference
import kotlin.reflect.KFunction1

internal const val EXTENSION_CLASS_DESCRIPTOR = "Lapp/morphe/extension/shared/GmsCoreSupport;"

internal const val GMS_CORE_VENDOR_GROUP_ID = "app.revanced"

/**
 * A patch that allows patched Google apps to run without root and under a different package name
 * by using GmsCore instead of Google Play Services.
 *
 * @param spoofedPackageSignature The signature of the package to spoof to.
 * @param mainActivityName The activity class containing onCreate where we will inject our extension (uses Smali naming and standard string comparison conventions).
 * @param fromPackageName The package name of the original app.
 * @param toPackageName The package name to fall back to if no custom package name is specified in patch options.
 * @param earlyReturnFingerprints The fingerprints of methods that need to be returned early.
 * @param executeBlock The additional execution block of the patch.
 * @param block The additional block to build the patch.
 */
fun gmsCoreSupportPatch(
    spoofedPackageSignature: String,
    mainActivityName: String,
    fromPackageName: String? = null,
    toPackageName: String? = null,
    earlyReturnFingerprints: Set<Fingerprint> = setOf(),
    executeBlock: BytecodePatchContext.() -> Unit = {},
    block: BytecodePatchBuilder.() -> Unit = {},
) = bytecodePatch(
    name = "MicroG integration",
    description = "Allows the app to work without root by using MicroG instead of Google Play Services.",
) {
    val changePackageName = !fromPackageName.isNullOrBlank() && !toPackageName.isNullOrBlank()

    dependsOn(
        addResourcesPatch,
        gmsCoreSupportResourcePatch(spoofedPackageSignature, fromPackageName, toPackageName),
        sharedExtensionPatch(activityOnCreateExtensionHook(mainActivityName)),
    )

    if (changePackageName) dependsOn(changePackageNamePatch)

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
        if (changePackageName) {
            val packageName = setOrGetFallbackPackageName(toPackageName)
            transformations.add(packageNameTransform(fromPackageName, packageName) as KFunction1<String, String?>)
        }

        transformStringReferences transform@{ string ->
            transformations.forEach { transform ->
                transform(string)?.let { transformedString -> return@transform transformedString }
            }

            return@transform null
        }

        // Return these methods early to prevent the app from crashing.
        earlyReturnFingerprints.forEach {
            it.method.apply {
                if (returnType == "Z") {
                    returnEarly(false)
                } else {
                    returnEarly()
                }
            }
        }
        ServiceCheckFingerprint.method.returnEarly()

        // Return status code 0 for play service availability checks.
        listOf(IsGooglePlayServicesAvailableFingerprint, GooglePlayUtilityFingerprint).forEach {
            it.methodOrNull?.apply { returnEarly(0) }
        }

        // Set original and patched package names for extension to use.
        OriginalPackageNameExtensionFingerprint.method.returnEarly(fromPackageName ?: packageMetadata.packageName)

        // Verify GmsCore is installed and whitelisted for power optimizations and background usage.
        getMainOnCreateFingerprint(mainActivityName).method.apply {
            addInstruction(
                0,
                "invoke-static/range { p0 .. p0 }, $EXTENSION_CLASS_DESCRIPTOR->" +
                        "checkGmsCore(Landroid/app/Activity;)V"
            )
        }

        // Change the vendor of GmsCore in the extension.
        GmsCoreSupportFingerprint.method.returnEarly(GMS_CORE_VENDOR_GROUP_ID)

        executeBlock()
    }

    block()
}