package hoodles.morphe.patches.teuida.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Teuida",
        packageName = "net.teuida.teuida",
        appIconColor = 0x52DBD7,
        targets = listOf(AppTarget("1.21.16"))
    )
}