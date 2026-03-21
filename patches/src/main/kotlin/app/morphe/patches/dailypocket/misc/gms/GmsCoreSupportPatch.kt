package app.morphe.patches.dailypocket.misc.gms

import app.morphe.patches.shared.misc.gms.gmsCoreSupportPatch

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    mainActivityName = "/MainActivity;",
    spoofedPackageSignature = "838ec2c270f8ca934e03b4894f4c15b6a4a4f3fd",
) {
    compatibleWith("kr.co.yjteam.dailypay")
}