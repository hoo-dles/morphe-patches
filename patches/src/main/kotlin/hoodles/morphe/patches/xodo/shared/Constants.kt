package hoodles.morphe.patches.xodo.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Xodo",
        packageName = "com.xodo.pdf.reader",
        appIconColor = 0xc82c40,
        targets = listOf(AppTarget("10.15.0"))
    )
}