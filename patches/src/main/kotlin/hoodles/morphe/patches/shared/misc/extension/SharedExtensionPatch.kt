/**
 * Original code is credited to Morphe:
 * https://github.com/MorpheApp/morphe-patches-https://github.com/MorpheApp/morphe-patches/blob/main/patches/src/main/kotlin/app/morphe/patches/all/misc/resources/AddResourcesPatch.ktlibrary/blob/main/patch-library/src/main/kotlin/app/morphe/patches/all/misc/resources/ResourceMappingPatch.kt
 *
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.extension

import app.morphe.patcher.patch.Patch
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.extension.ExtensionHook
import app.morphe.patches.all.misc.extension.SHARED_UTILS_EXTENSION_CLASS
import app.morphe.util.returnEarly
import java.net.URLDecoder
import java.util.function.Supplier
import java.util.jar.JarFile

fun sharedExtensionPatch(
    extensionName: String,
    hook: () -> ExtensionHook,
    dependsOn: List<Patch<*>> = emptyList()
) = createSharedExtensionPatch(
    listOf("shared", extensionName),
    hook,
    dependsOn
)

@Suppress("CheckResult")
private fun createSharedExtensionPatch(
    extensionName: List<String>,
    hook: () -> ExtensionHook,
    dependsOn: List<Patch<*>>
) = bytecodePatch {
    // Check if patcher supports loading multiple extension input streams and warn if not.
    val supportsMultiDex = runCatching {
        javaClass.getDeclaredMethod("extendWith", Supplier::class.java)
    }.isSuccess

    if (supportsMultiDex) {
        for (name in extensionName) {
            extendWith("extensions/$name.mpe")
        }
    } else {
        for (name in extensionName.toMutableSet().also { it.add("shared") }) {
            dependsOn(bytecodePatch { extendWith("extensions/$name.mpe") })
        }
    }

    dependsOn(*dependsOn.toTypedArray())

    execute {
        // Verify the extension class exists.
        classDefBy(SHARED_UTILS_EXTENSION_CLASS)
    }

    finalize {
        // The hooks are made in finalize to ensure that the context is hooked before any other patches.
        hook()(SHARED_UTILS_EXTENSION_CLASS)

        // Modify Utils method to include the patches release version.
        MorpheUtilsPatchesVersionFingerprint.method.apply {
            /**
             * @return The file path for the jar this classfile is contained inside.
             */
            fun getCurrentJarFilePath(): String {
                val className = object {}::class.java.enclosingClass.name.replace('.', '/') + ".class"
                val classUrl = object {}::class.java.classLoader?.getResource(className)
                if (classUrl != null) {
                    val urlString = classUrl.toString()

                    if (urlString.startsWith("jar:file:")) {
                        val end = urlString.lastIndexOf('!')

                        return URLDecoder.decode(urlString.substring("jar:file:".length, end), "UTF-8")
                    }
                }
                throw IllegalStateException("Not running from inside a JAR file.")
            }

            /**
             * @return The value for the manifest entry,
             *         or "Unknown" if the entry does not exist or is blank.
             */
            @Suppress("SameParameterValue")
            fun getPatchesManifestEntry(attributeKey: String) = JarFile(getCurrentJarFilePath()).use { jarFile ->
                jarFile.manifest.mainAttributes.entries.firstOrNull { it.key.toString() == attributeKey }?.value?.toString()
                    ?: "Unknown"
            }

            val manifestValue = getPatchesManifestEntry("Version")
            returnEarly(manifestValue)
        }
    }
}