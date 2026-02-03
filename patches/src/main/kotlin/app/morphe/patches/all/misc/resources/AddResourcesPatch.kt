package app.morphe.patches.all.misc.resources

import app.morphe.patcher.patch.resourcePatch
import app.morphe.util.forEachChildElement
import app.morphe.util.getNode
import app.morphe.util.inputStreamFromBundledResource
import app.morphe.util.resource.StringResource.Companion.sanitizeAndroidResourceString
import java.util.Locale
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.jvm.javaClass

internal val locales = listOf(
    AppLocale("", ""), // Default English locale. Must be first.
)

internal class AppLocale(
    private val srcLocale: String,
    private val destLocale: String,
    val isBuiltInLanguage: Boolean = true
) {
    fun isDefaultLocale() = srcLocale.isEmpty()

    fun getSrcLocaleFolderName() = getValuesFolderName(srcLocale)
    fun getDestLocaleFolderName() = getValuesFolderName(destLocale)

    override fun toString(): String {
        return "AppLocale(srcLocale='${getSrcLocaleFolderName()}', destLocale='${getDestLocaleFolderName()}', " +
                "isBuiltInLanguage=$isBuiltInLanguage)"
    }

    private companion object {
        private fun getValuesFolderName(localeName: String): String {
            val folderName = "values"

            return if (localeName.isEmpty()) {
                folderName
            } else {
                "$folderName-$localeName"
            }
        }
    }
}

private enum class BundledResourceType {
    STRINGS,
    ARRAYS;

    override fun toString(): String {
        return super.toString().lowercase(Locale.US)
    }
}

private val appsToInclude = mutableSetOf<String>()

/**
 * Add all resources for the given app.
 */
internal fun addAppResources(appId: String) {
    appsToInclude.add(appId)
}

internal val addResourcesPatch = resourcePatch(
    description = "Add resources such as strings or arrays to the app."
) {
    val defaultResourcesAdded = mutableSetOf<String>()

    finalize {
        fun getLogger(): Logger = Logger.getLogger(AppLocale.javaClass.name)

        fun addResourcesFromFile(
            appId: String,
            locale: AppLocale,
            resourceType: BundledResourceType
        ) {
            val isDefaultLocale = locale.isDefaultLocale()
            val srcFolderName = locale.getSrcLocaleFolderName()
            val srcSubPath = "$srcFolderName/$appId/$resourceType.xml"
            val destSubPath = "res/${locale.getDestLocaleFolderName()}/$resourceType.xml"

            val srcStream = inputStreamFromBundledResource(
                "addresources", srcSubPath
            )

            if (srcStream == null) {
                // Localized arrays are optional, but string files are expected.
                if (resourceType == BundledResourceType.STRINGS) {
                    throw IllegalArgumentException("Could not find: $srcSubPath")
                }
                return
            }

            srcStream.use {
                val destFile = this@finalize[destSubPath]
                if (!destFile.exists()) {
                    if (locale.isBuiltInLanguage) {
                        throw IllegalStateException(
                            "Expected to find locale: $locale but file does not exist in target app: $destFile"
                        )
                    }

                    destFile.parentFile?.mkdirs()
                    if (!destFile.createNewFile()) throw IllegalStateException()
                    destFile.writeText(
                        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n</resources>"
                    )
                }

                document(destSubPath).use { destDoc ->
                    val destResourceNode = destDoc.getNode("resources")

                    document(srcStream).use { srcDoc ->
                        // Check for bad localized files with duplicate strings.
                        val localeStringsAdded = mutableSetOf<String>()

                        srcDoc.getElementsByTagName(
                            "resources"
                        ).item(0)?.forEachChildElement { srcNode ->
                            val resourceName = srcNode.getAttributeNode("name").value
                            if (resourceType == BundledResourceType.STRINGS) {
                                // Check for bad text strings that will fail resource compilation.
                                val textContent = srcNode.textContent
                                val sanitized = sanitizeAndroidResourceString(
                                    resourceName, textContent, destSubPath
                                )
                                if (textContent != sanitized) {
                                    srcNode.textContent = sanitized
                                }
                            }

                            if (!localeStringsAdded.add(resourceName)) {
                                getLogger().warning(
                                    "Duplicate string resource is declared: $srcFolderName " +
                                            "resource: $resourceName"
                                )
                                return@forEachChildElement
                            }

                            if (isDefaultLocale) {
                                // Duplicate check alreday handled above.
                                defaultResourcesAdded.add(resourceName)
                            } else if (!defaultResourcesAdded.contains(resourceName)) {
                                // TODO: Enable when patcher/CLI supports debug/dev logging.
                                if (false) getLogger().log(Level.INFO) {
                                    "Ignoring removed default resource for locale (Issue will be fixed after next Crowdin sync): " +
                                            "$srcFolderName resource: $resourceName"
                                }
                                return@forEachChildElement
                            }

                            val importedSrcNode = destDoc.importNode(srcNode, true)
                            destResourceNode.appendChild(importedSrcNode)
                        }
                    }
                }
            }
        }

        appsToInclude.forEach { app ->
            locales.forEach { locale ->
                addResourcesFromFile(app, locale, BundledResourceType.STRINGS)
                addResourcesFromFile(app, locale, BundledResourceType.ARRAYS)
            }
        }
    }
}