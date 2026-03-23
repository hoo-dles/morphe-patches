package app.morphe.patches.primevideo.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Prive Video",
        packageName = "com.amazon.avod.thirdpartyclient",
        targets = listOf(AppTarget("3.0.443"))
    )
}