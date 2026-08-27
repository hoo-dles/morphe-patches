/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.lightroom.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Lightroom Mobile",
        packageName = "com.adobe.lrmobile",
        appIconColor = 0x001d35,
        targets = listOf(AppTarget("11.5.01"))
    )
}