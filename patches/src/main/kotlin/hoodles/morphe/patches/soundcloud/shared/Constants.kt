/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.soundcloud.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "SoundCloud",
        packageName = "com.soundcloud.android",
        appIconColor = 0x000000,
        targets = listOf(AppTarget("2026.08.26-release"))
    )
}