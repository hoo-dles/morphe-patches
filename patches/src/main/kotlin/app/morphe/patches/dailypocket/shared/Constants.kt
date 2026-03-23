package app.morphe.patches.dailypocket.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Daily Pocket",
        packageName = "kr.co.yjteam.dailypay",
        targets = listOf(AppTarget("6.0.7"))
    )

    const val SIGNATURE = "838ec2c270f8ca934e03b4894f4c15b6a4a4f3fd"
}