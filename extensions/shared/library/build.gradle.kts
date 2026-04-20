plugins {
    alias(libs.plugins.android.library) 
}

android {
    namespace = "hoodles.morphe.extension.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    compileOnly(libs.annotation)
    implementation(libs.morphe.extensions.library)
}
