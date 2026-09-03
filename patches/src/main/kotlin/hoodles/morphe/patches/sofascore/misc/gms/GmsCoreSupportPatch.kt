package hoodles.morphe.patches.sofascore.misc.gms

import hoodles.morphe.patches.shared.misc.gms.gmsCoreSupportPatch
import hoodles.morphe.patches.sofascore.shared.Constants

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    mainActivityName = "/StartActivity;",
    spoofedPackageSignature = "e235776bc0ad3837b01359177c438658b1424d23",
) {
    compatibleWith(Constants.COMPATIBILITY)
}