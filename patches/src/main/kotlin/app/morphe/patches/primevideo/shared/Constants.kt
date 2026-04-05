package app.morphe.patches.primevideo.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Prime Video",
        packageName = "com.amazon.avod.thirdpartyclient",
        appIconColor = 0x177BCE,
        targets = listOf(AppTarget("3.0.447.757"))
    )
}