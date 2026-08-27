plugins {
    base
}

tasks {
    val codeGenPath = project.project("extensions").projectDir.resolve("__generated__")

    val cleanGenerated = register<Delete>("cleanGenerated") {
        description = "Cleans code generated files. Does not remove `__generated__` directory"
        delete(fileTree(codeGenPath) {
            include("**/*")
        })
    }

    register<GeneratePairipExtensionTask>("codegen") {
        description = "Generates extension modules based on reflection mappings defined in " +
                "`patches/resources/pairip/[app_name].json`."

        doFirst {
            codeGenPath.mkdirs()

            val isWindows = System.getProperty("os.name").lowercase().contains("win")
            if (isWindows) {
                val isCaseSensitive = providers.exec {
                    executable("fsutil.exe")
                    args("file", "queryCaseSensitiveInfo", codeGenPath.absolutePath)
                }.standardOutput.asText.get().contains("enabled")

                if (!isCaseSensitive) {
                    // This MUST be run with elevated privileges. The easiest method is to stop the
                    // Gradle daemon, and run the task in an Administrator command prompt.
                    providers.exec {
                        executable("fsutil.exe")
                        args("file", "setCaseSensitiveInfo", codeGenPath.absolutePath, "enable")
                    }
                }
            }
        }

        dependsOn(cleanGenerated)
    }

    named<Delete>("clean") {
        dependsOn(cleanGenerated)
    }
}