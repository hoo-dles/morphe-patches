package app.morphe.patches.windy.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.util.returnEarly

private const val ASSET_PATH = "assets/public/v"
private val jsPatch = rawResourcePatch {
    execute {
        val versionsDir = get(ASSET_PATH)
        // Get most recent directory (although there should only be one)
        val jsDir = versionsDir.listFiles { it.isDirectory }?.maxByOrNull { it.lastModified() }?.name
        checkNotNull(jsDir) { "Cordova application directory not found." }

        val jsFile = get("$ASSET_PATH/$jsDir/mobile.js", true)

        val replacements = mapOf(
            // Patch `hasAny()` function
            """null!==\w+\.get\("subscription"\),""".toRegex() to "true,",
            // Patch logic run when null subscription is set
            """\w+\.set\("detail1h",!1\),""".toRegex() to "",
            """set\("subscription",null\),""".toRegex() to """set("subscription","premium"),""",
            """\w+&&document\.body\.classList\.remove\("subs-"\.concat\(\w+\)\),""".toRegex() to """document.body.classList.add("subs-premium"),"""
        )

        var patchedContent = replacements.entries.fold(jsFile.readText()) { acc, entry ->
            acc.replace(entry.key, entry.value)
        }

        jsFile.writeText(patchedContent)
    }
}

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables some app features locked behind the subscription paywall. Not all premium functionality is available."
) {
    compatibleWith("com.windyty.android"("49.0.1"))

    dependsOn(jsPatch)

    execute {
        IsPremiumForWidgetFingerprint.method.returnEarly(true)
    }
}