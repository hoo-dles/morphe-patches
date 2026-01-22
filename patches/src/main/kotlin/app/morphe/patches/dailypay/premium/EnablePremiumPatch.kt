package app.morphe.patches.dailypay.premium

import app.morphe.patcher.patch.rawResourcePatch

@Suppress("unused")
val enablePremiumPatch = rawResourcePatch(
    name = "Enable Premium"
) {
    compatibleWith("kr.co.yjteam.dailypay")
    dependsOn(premiumWidgetPatch)

    execute {
        var indexFile = get("assets/www/assets")
            .listFiles()!!
            .first { file -> file.name.startsWith("index") && file.extension == "js" }

        var patchedCode = indexFile.readText().replace(
            Regex("""await [a-zA-Z]+\.asyncNativeStorageGetItem\("isPayment"\)==="true""""),
            "true"
        )

        indexFile.writeText(patchedCode)
    }
}