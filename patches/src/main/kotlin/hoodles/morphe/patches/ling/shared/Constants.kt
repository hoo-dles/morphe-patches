package hoodles.morphe.patches.ling.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Ling",
        packageName = "com.simyasolutions.ling.universal",
        // Cosmetic only (patcher UI accent). Ling's brand is coral red.
        appIconColor = 0xF8485E,
        targets = listOf(AppTarget("8.4.3"))
    )
}
