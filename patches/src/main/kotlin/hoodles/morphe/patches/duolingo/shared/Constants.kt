package hoodles.morphe.patches.duolingo.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Duolingo",
        packageName = "com.duolingo",
        appIconColor = 0x58CC02,
        targets = listOf(AppTarget("6.85.7"))
    )
}