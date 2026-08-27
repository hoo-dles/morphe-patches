/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.FileOutput
import gg.jte.resolve.ResourceCodeResolver
import hoodles.morphe.codegen.pairip.JavaField
import hoodles.morphe.codegen.pairip.JavaCodegenData
import hoodles.morphe.codegen.pairip.getClassFromFqcn
import hoodles.morphe.codegen.pairip.getPackageFromFqcn
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GeneratePairipExtensionTask : DefaultTask() {

    @Suppress("NewApi")
    @TaskAction
    fun generate() {
        val jsonMaps = project.project("patches").fileTree("pairip") {
            include("*.json")
        }

        val genExtRoot = project.project("extensions").projectDir.resolve("__generated__")

        val codeResolver = ResourceCodeResolver("pairip")
        val templateEngine = TemplateEngine.create(
            codeResolver,
            project.layout.buildDirectory.dir("jte-classes").get().asFile.toPath(),
            ContentType.Plain,
            javaClass.classLoader
        )
        templateEngine.setTrimControlStructures(true)

        jsonMaps.forEach { file ->
            val appName = file.nameWithoutExtension
            val data = Json.decodeFromString<JavaCodegenData>(file.readText())

            // Extension code
            val appExtRoot = genExtRoot.resolve(appName)
            val appExtMain = appExtRoot.resolve("src/main")
            val appExtPairip = appExtMain.resolve("java/hoodles/morphe/extension/$appName/pairip")
            appExtPairip.mkdirs()

            writeResourceToFile(
                "pairip/build.gradle.kts.template",
                appExtRoot.resolve("build.gradle.kts"),
                mapOf("{{APP}}" to appName)
            )
            writeResourceToFile(
                "pairip/AndroidManifest.xml",
                appExtMain.resolve("AndroidManifest.xml")
            )

            val hookParams = mapOf(
                "data" to data,
                "appName" to appName,
            )

            val hookClass = appExtPairip.resolve("PairipHook.java")
            FileOutput(hookClass.toPath()).use { output ->
                templateEngine.render("PairipHook.jte", hookParams, output)
            }

            // Stubs
            val appStubRoot = appExtRoot.resolve("stub")
            val appStubMain = appStubRoot.resolve("src/main")
            val appStubJava = appStubMain.resolve("java")
            appStubJava.mkdirs()

            writeResourceToFile(
                "pairip/stub.build.gradle.kts",
                appStubRoot.resolve("build.gradle.kts")
            )
            writeResourceToFile(
                "pairip/AndroidManifest.xml",
                appStubMain.resolve("AndroidManifest.xml")
            )

            fun generateStubClasses(entries: Map<String, List<JavaField>>, jteTemplate: String) {
                for ((fqcn, fields) in entries) {
                    val packageName = getPackageFromFqcn(fqcn)
                    val packagePath = appStubJava.resolve(packageName.replace(".", "/"))
                    packagePath.mkdirs()

                    val className = getClassFromFqcn(fqcn)
                    val stubParams = mapOf(
                        "packageName" to packageName,
                        "className" to className,
                        "fieldNames" to fields.map { it.name }
                    )

                    val stubClass = packagePath.resolve("$className.java")
                    FileOutput(stubClass.toPath()).use { output ->
                        templateEngine.render(jteTemplate, stubParams, output)
                    }
                }
            }

            generateStubClasses(data.strings, "StringClassStub.jte")
            generateStubClasses(data.methods, "MethodClassStub.jte")
        }
    }

    private fun writeResourceToFile(resourcePath: String, targetFile: File, replacements: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.getResourceAsStream(resourcePath)
            ?: throw IllegalArgumentException("Could not find pairip asset: $resourcePath")

        val template = inputStream.bufferedReader().use { it.readText() }
        val contents = replacements.entries.fold(template) { acc, entry ->
            acc.replace(entry.key, entry.value)
        }

        targetFile.writeText(contents)
    }
}