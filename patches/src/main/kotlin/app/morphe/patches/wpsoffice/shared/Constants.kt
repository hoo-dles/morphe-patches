package app.morphe.patches.wpsoffice.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "WPS Office",
        packageName = "cn.wps.moffice_eng",
        targets = listOf(AppTarget("18.24"))
    )
}