group = "hoodles.morphe"

patches {
    about {
        name = "Hoodles Morphe Patches"
        description = "Various patches for use with Morphe"
        source = "git@github.com:hoo-dles/morphe-patches.git"
        author = "hoodles"
        contact = "na"
        website = "https://morphe.software"
        license = "GPLv3"
    }
}

repositories {
    google()
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/MorpheApp/registry")
        credentials {
            username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
            password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
        }
    }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // Used by JsonGenerator.
    implementation(libs.gson)
    // Required due to smali, or build fails. Can be removed once smali is bumped.
    implementation(libs.guava)
    implementation(libs.morphe.patches.library)

    // Used to read exported symbol from ELF libraries
    implementation(libs.jelf)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.native.lib.loader) {
        // bundled version clashes with newer runtime dependency
        exclude(group = "org.slf4j", module = "slf4j-api")
    }

    compileOnly(project(":patches:stub"))
    compileOnly(libs.android.all)
    compileOnly(libs.slf4j.api)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("app.morphe.util.PatchListGeneratorKt")
    }
    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-parameters")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }

    jvmToolchain(17)
}

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}

plugins {
    kotlin("plugin.serialization") version "2.4.10"
}
