package hoodles.morphe.patches.podcastaddict.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Podcast Addict",
        packageName = "com.bambuna.podcastaddict",
        appIconColor = 0xFD7E14,
        targets = listOf(AppTarget("2026.4"))
    )
}