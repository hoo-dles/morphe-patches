/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.alltrails.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "AllTrails",
        packageName = "com.alltrails.alltrails",
        appIconColor = 0x64F67A,
        targets = listOf(
            AppTarget("26.3.20"),
            AppTarget("26.6.20"),
            AppTarget("26.7.40", isExperimental = true)
        )
    )
}
