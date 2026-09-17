android {
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    implementation(libs.annotation)
    compileOnly(project(":extensions:shared:library"))
}