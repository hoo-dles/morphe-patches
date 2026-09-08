/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.xrecorder.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "XRecorder",
        packageName = "videoeditor.videorecorder.screenrecorder",
        appIconColor = 0xf76219,
        targets = listOf(AppTarget("2.5.1.1"))
    )
}