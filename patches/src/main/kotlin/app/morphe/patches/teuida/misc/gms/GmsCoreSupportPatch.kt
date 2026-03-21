package app.morphe.patches.teuida.misc.gms

import app.morphe.patches.shared.misc.gms.gmsCoreSupportPatch

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    mainActivityName = "/EntryActivity;",
    spoofedPackageSignature = "67bd96b0fff3989af9ed068ec9bd9bbe7583b03b",
) {
    compatibleWith("net.teuida.teuida")
}