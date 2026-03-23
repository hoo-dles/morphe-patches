package app.morphe.patches.solidexplorer.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Solid Explorer",
        packageName = "pl.solidexplorer2",
        targets = listOf(AppTarget("3.4.1"))
    )
}