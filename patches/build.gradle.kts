group = "app.morphe"

patches {
    about {
        name = "XYZ Patches for use with Morphe"
        description = "Example patches"
        source = "git@github.com:MorpheApp/morphe-patches-template.git"
        author = "Awesome dev"
        contact = "na"
        website = "https://morphe.software"
        license = "GPLv3"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
