package app.morphe.patches.shared.misc.gms

import app.morphe.patcher.patch.resourcePatch
import app.morphe.patches.all.misc.packagename.changePackageNamePatch
import app.morphe.patches.all.misc.packagename.setOrGetFallbackPackageName
import org.w3c.dom.Element
import org.w3c.dom.Node

/**
 * Abstract resource patch that allows Google apps to run without root and under a different package name
 * by using GmsCore instead of Google Play Services.
 *
 * @param spoofedPackageSignature The signature of the package to spoof to.
 * @param fromPackageName The package name of the original app.
 * @param toPackageName The package name to fall back to if no custom package name is specified in patch options.
 */
fun gmsCoreSupportResourcePatch(
    spoofedPackageSignature: String,
    fromPackageName: String? = null,
    toPackageName: String? = null,
) = resourcePatch {

    val changePackageName = !fromPackageName.isNullOrBlank() && !toPackageName.isNullOrBlank()

    if (changePackageName) dependsOn(changePackageNamePatch)

    execute {
        /**
         * Add metadata to manifest to support spoofing the package name and signature of GmsCore.
         */
        fun addSpoofingMetadata() {
            fun Node.adoptChild(
                tagName: String,
                block: Element.() -> Unit,
            ) {
                val child = ownerDocument.createElement(tagName)
                child.block()
                appendChild(child)
            }

            document("AndroidManifest.xml").use { document ->
                val applicationNode =
                    document
                        .getElementsByTagName("application")
                        .item(0)

                // Spoof package name
                if (changePackageName) {
                    applicationNode.adoptChild("meta-data") {
                        setAttribute("android:name", "$GMS_CORE_VENDOR_GROUP_ID.android.gms.SPOOFED_PACKAGE_NAME")
                        setAttribute("android:value", fromPackageName)
                    }
                }

                // Spoof package signature
                applicationNode.adoptChild("meta-data") {
                    setAttribute("android:name", "$GMS_CORE_VENDOR_GROUP_ID.android.gms.SPOOFED_PACKAGE_SIGNATURE")
                    setAttribute("android:value", spoofedPackageSignature)
                }

                // GmsCore presence detection in extension.
                applicationNode.adoptChild("meta-data") {
                    // TODO: The name of this metadata should be dynamic.
                    setAttribute("android:name", "app.revanced.MICROG_PACKAGE_NAME")
                    setAttribute("android:value", "$GMS_CORE_VENDOR_GROUP_ID.android.gms")
                }
            }
        }

        /**
         * Patch the manifest to support GmsCore.
         */
        fun patchManifest() {
            val transformations = mutableMapOf(
                "com.google.android.c2dm" to "$GMS_CORE_VENDOR_GROUP_ID.android.c2dm",
                "com.google.android.libraries.photos.api.mars" to "$GMS_CORE_VENDOR_GROUP_ID.android.apps.photos.api.mars",
                "</queries>" to "<package android:name=\"$GMS_CORE_VENDOR_GROUP_ID.android.gms\"/></queries>",
            )

            if (changePackageName) {
                val packageName = setOrGetFallbackPackageName(toPackageName)

                transformations.putAll(mapOf(
                    "package=\"$fromPackageName" to "package=\"$packageName",
                    "android:authorities=\"$fromPackageName" to "android:authorities=\"$packageName",
                    "$fromPackageName.permission.C2D_MESSAGE" to "$packageName.permission.C2D_MESSAGE",
                    "$fromPackageName.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION" to "$packageName.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION",
                ))
            }

            val manifest = get("AndroidManifest.xml")
            manifest.writeText(
                transformations.entries.fold(manifest.readText()) { acc, (from, to) ->
                    acc.replace(
                        from,
                        to,
                    )
                },
            )
        }

        patchManifest()
        addSpoofingMetadata()
    }
}
