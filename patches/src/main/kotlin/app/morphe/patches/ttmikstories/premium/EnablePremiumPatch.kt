package app.morphe.patches.ttmikstories.premium

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.shared.misc.hermes.hermesPatch

@Suppress("unused")
val enablePremiumPatch = rawResourcePatch(
    name = "Enable Premium"
) {
    compatibleWith("app.ttmikstories.android"("1.16.0"))

    dependsOn(hermesPatch {
        //  Call1           r3, r3, r4
        //      --> LoadConstTrue r3
        //      --> Nop
        //      --> Nop
        //  PutNewOwnById   r0, r3, "isWhiteListUser"
        setOf("51 03 03 04 40 00 03 F5 77" to "78 03 61 61 40 00 03 F5 77")
    })
}