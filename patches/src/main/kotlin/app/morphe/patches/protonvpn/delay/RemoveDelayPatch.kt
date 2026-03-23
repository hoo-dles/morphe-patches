package app.morphe.patches.protonvpn.delay

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.protonvpn.shared.Constants
import app.morphe.util.returnEarly

@Suppress("unused")
val removeChangeServerDelayPatch = bytecodePatch(
    name = "Remove delay",
    description = "Removes the imposed delay when changing VPN servers."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        GetLongDelayFingerprint.method.returnEarly(0)
        GetShortDelayFingerprint.method.returnEarly(0)
    }
}