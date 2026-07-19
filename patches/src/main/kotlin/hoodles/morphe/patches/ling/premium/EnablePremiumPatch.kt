package hoodles.morphe.patches.ling.premium

import app.morphe.patcher.patch.rawResourcePatch
import hoodles.morphe.patches.ling.shared.Constants
import hoodles.morphe.patches.shared.misc.hermes.hermesPatch

const val RETURN_TRUE = "78 00 5C 00"

@Suppress("unused")
val enablePremiumPatch = rawResourcePatch(
    name = "Enable Premium",
    description = "Unlocks Ling Pro"
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(hermesPatch {
        val selectIsProUser =
            "6C 00 01 37 00 00 01 A9 7B 37 00 00 02 92 7C 5C 00" to RETURN_TRUE

        setOf(selectIsProUser)
    })
}
