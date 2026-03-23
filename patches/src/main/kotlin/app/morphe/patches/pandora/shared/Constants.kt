package app.morphe.patches.pandora.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Pandora",
        packageName = "com.pandora.android",
        targets = listOf(AppTarget(null))
    )
}