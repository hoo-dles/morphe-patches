package app.morphe.patches.cake.plus

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.shared.misc.hermes.hermesPatch

//  LoadConstTrue         r0
//  Ret                   r0
const val RETURN_TRUE = "78 00 5c 00"

@Suppress("unused")
val enablePlusPatch = rawResourcePatch(
    name = "Enable Plus",
    description = "Enable Plus membership (not all features are available)."
) {
    compatibleWith("me.mycake"("6.4.0"))

    dependsOn(hermesPatch {
        //  GetEnvironment        r0, 0
        //  LoadFromEnvironment   r0, r0, 0
        //  GetById               r0, r0, 1, "_authData"
        //  LoadConstNull         r1
        //  Eq                    r1, r0, r1
        val hasMembership = "29 00 00 2E 00 00 00 37 00 00 01 EB C7 77 01 0E 01 00 01" to RETURN_TRUE

        //  CreateEnvironment     r0
        //  LoadThisNS            r3
        //  StoreToEnvironment    r0, 0, r3
        //  GetById               r2, r3, 1, "_getMembershipCache"
        //  LoadConstString       r1, "HAS_CAKE"
        val hasCake = "32 00 7C 03 2A 00 00 03 37 02 03 01 F0 BF 73 01 F4 1C" to RETURN_TRUE

        setOf(hasMembership, hasCake)
    })
}