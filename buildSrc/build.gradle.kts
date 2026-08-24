plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version "2.4.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jte)
    implementation(libs.kotlinx.serialization.json)
}