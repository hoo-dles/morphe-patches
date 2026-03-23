package app.morphe.patches.nomone.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "NOMone Desktop",
        packageName = "nom.vrd",
        targets = listOf(
            AppTarget("1.9.3-GooglePlay"),
            AppTarget("1.9.3-storage")
        )
    )
}