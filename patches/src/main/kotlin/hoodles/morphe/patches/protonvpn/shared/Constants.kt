package hoodles.morphe.patches.protonvpn.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Proton VPN",
        packageName = "ch.protonvpn.android",
        appIconColor = 0x6D4AFF,
        targets = listOf(AppTarget("5.17.72.0"))
    )
}