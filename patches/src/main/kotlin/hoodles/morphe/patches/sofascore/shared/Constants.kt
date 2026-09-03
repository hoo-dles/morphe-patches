package hoodles.morphe.patches.sofascore.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Sofascore",
        packageName = "com.sofascore.results",
        appIconColor = 0x374DF5,
        targets = listOf(AppTarget("26.08.18"))
    )
}