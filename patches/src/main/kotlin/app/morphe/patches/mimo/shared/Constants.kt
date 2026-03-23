package app.morphe.patches.mimo.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Mimo",
        packageName = "com.getmimo",
        targets = listOf(AppTarget("9.0"))
    )

    val SIGNATURE = "93D53764C40AEB53E09A306D01D74DFF11412021"
}