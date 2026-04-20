package hoodles.morphe.patches.xodo.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Xodo",
        packageName = "com.xodo.pdf.reader",
        appIconColor = 0xFFFFFF,
        targets = listOf(AppTarget("10.13.0"))
    )
}