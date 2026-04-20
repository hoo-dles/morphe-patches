package hoodles.morphe.patches.protonvpn.delay

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.protonvpn.shared.Constants

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