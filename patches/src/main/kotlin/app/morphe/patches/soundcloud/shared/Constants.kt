package app.morphe.patches.soundcloud.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "SoundCloud",
        packageName = "com.soundcloud.android",
        targets = listOf(AppTarget("2026.03.20-release"))
    )
}