package app.morphe.patches.solidexplorer.misc.gms

import app.morphe.patches.shared.misc.gms.gmsCoreSupportPatch

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    mainActivityName = "/SolidExplorer;",
    spoofedPackageSignature = "e4e2c621308f43912674e8c00cd5c1408c55e5a0",
) {
    compatibleWith("pl.solidexplorer2")
}

