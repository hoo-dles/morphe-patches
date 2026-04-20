package hoodles.morphe.patches.teuida.misc.gms

import hoodles.morphe.patches.teuida.shared.Constants
import hoodles.morphe.patches.shared.misc.gms.gmsCoreSupportPatch

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    mainActivityName = "/EntryActivity;",
    spoofedPackageSignature = "67bd96b0fff3989af9ed068ec9bd9bbe7583b03b",
) {
    compatibleWith(Constants.COMPATIBILITY)
}