/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.ling.premium

import app.morphe.patcher.patch.rawResourcePatch
import hoodles.morphe.compatibility.Compat
import hoodles.morphe.patches.shared.misc.hermes.hermesPatch
import hoodles.morphe.patches.shared.misc.pairip.getStripPairipPatch
import hoodles.morphe.util.HermesConstants.RETURN_TRUE

@Suppress("unused")
val enablePremiumPatch = rawResourcePatch(
    name = "Enable Pro",
    description = "Enables app features locked behind the subscription paywall. Requirements: strict apk version"
) {
    compatibleWith(Compat.LING)

    dependsOn(getStripPairipPatch("ling"))

    dependsOn(hermesPatch {
//        LoadParam r0, 1
//        GetById   r0, r0, 1, "payments"
//        GetById   r0, r0, 2, "isProUser"
//        Ret       r0
        val selectIsProUser =
            "6C 00 01 37 00 00 01 A0 9D 37 00 00 02 81 E6 5C 00" to RETURN_TRUE

        setOf(selectIsProUser)
    })
}
