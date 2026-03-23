package app.morphe.patches.eggbun.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY = Compatibility(
        name = "Eggbun",
        packageName = "kr.eggbun.eggconvo",
        targets = listOf(AppTarget("4.12.19"))
    )
}