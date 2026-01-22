group = "app.morphe"

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

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

dependencies {
    // Required due to smali, or build fails. Can be removed once smali is bumped.
    implementation(libs.guava)

    compileOnly(project(":patches:stub"))
}