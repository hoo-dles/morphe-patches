package app.morphe.patches.protonvpn.delay

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val removeChangeServerDelayPatch = bytecodePatch(
    name = "Remove server change delay"
) {
    compatibleWith("ch.protonvpn.android")

    execute {
        GetLongDelayFingerprint.method.returnEarly(0)
        GetShortDelayFingerprint.method.returnEarly(0)
    }
}