package hoodles.morphe.patches.ling.premium

import app.morphe.patcher.patch.rawResourcePatch
import hoodles.morphe.patches.ling.shared.Constants
import hoodles.morphe.patches.shared.misc.hermes.hermesPatch
import hoodles.morphe.util.HermesConstants.RETURN_TRUE

@Suppress("unused")
val enablePremiumPatch = rawResourcePatch(
    name = "Enable Pro",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(hermesPatch {
//        LoadParam r0, 1
//        GetById   r0, r0, 1, "payments"
//        GetById   r0, r0, 2, "isProUser"
//        Ret       r0
        val selectIsProUser =
            "6C 00 01 37 00 00 01 50 7B 37 00 00 02 29 7C 5C 00" to RETURN_TRUE

        setOf(selectIsProUser)
    })
}
