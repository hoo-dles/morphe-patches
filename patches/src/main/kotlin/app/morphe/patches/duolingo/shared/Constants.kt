package app.morphe.patches.duolingo.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Duolingo",
        packageName = "com.duolingo",
        targets = listOf(AppTarget("6.66.5"))
    )
}