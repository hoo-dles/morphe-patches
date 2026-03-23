package app.morphe.patches.protonvpn.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Proton VPN",
        packageName = "ch.protonvpn.android",
        targets = listOf(AppTarget("5.16.83.0"))
    )
}