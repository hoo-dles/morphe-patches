package app.morphe.patches.dailypocket.misc.gms

import app.morphe.patches.dailypocket.shared.Constants
import app.morphe.patches.shared.misc.gms.gmsCoreSupportPatch

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    mainActivityName = "/MainActivity;",
    spoofedPackageSignature = Constants.SIGNATURE,
) {
    compatibleWith(Constants.COMPATIBILITY)
}