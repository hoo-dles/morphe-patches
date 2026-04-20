package hoodles.morphe.patches.macrofactor.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    const val PACKAGE_NAME = "com.sbs.diet"
    const val WORKOUTS_PACKAGE_NAME = "com.sbs.train"

    val COMPATIBILITY = arrayOf(
        Compatibility(
            name = "Macro Factor",
            packageName = PACKAGE_NAME,
            appIconColor = 0x000000,
            targets = listOf(AppTarget("5.7.6"))
        ),
        Compatibility(
            name = "Macro Factor Workouts",
            packageName = WORKOUTS_PACKAGE_NAME,
            appIconColor = 0x000000,
            targets = listOf(AppTarget("1.1.7"))
        )
    )
}