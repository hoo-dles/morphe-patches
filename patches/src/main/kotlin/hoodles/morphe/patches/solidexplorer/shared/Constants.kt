/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.solidexplorer.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Solid Explorer",
        packageName = "pl.solidexplorer2",
        appIconColor = 0x039BE5,
        targets = listOf(AppTarget("3.5.19"))
    )
}