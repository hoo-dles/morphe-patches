package hoodles.morphe.patches.dailypocket.misc.gms

import hoodles.morphe.patches.dailypocket.shared.Constants
import hoodles.morphe.patches.shared.misc.gms.gmsCoreSupportPatch

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    mainActivityName = "/MainActivity;",
    spoofedPackageSignature = Constants.SIGNATURE,
) {
    compatibleWith(Constants.COMPATIBILITY)
}