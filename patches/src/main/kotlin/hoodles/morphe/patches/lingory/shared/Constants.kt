package hoodles.morphe.patches.lingory.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Lingory",
        packageName = "org.languageapp.lingory",
        appIconColor = 0x8F98FF,
        targets = listOf(AppTarget("1.2.75"))

    )
}