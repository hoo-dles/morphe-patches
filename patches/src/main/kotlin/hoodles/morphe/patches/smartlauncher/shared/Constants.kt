package hoodles.morphe.patches.smartlauncher.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Smart Launcher",
        packageName = "ginlemon.flowerfree",
        appIconColor = 0x000000,
        targets = listOf(AppTarget("6.6 build 002"))
    )
}