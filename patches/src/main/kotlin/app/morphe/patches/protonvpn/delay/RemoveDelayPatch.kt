package app.morphe.patches.protonvpn.delay

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val removeChangeServerDelayPatch = bytecodePatch(
    name = "Remove server change delay"
) {
    compatibleWith("ch.protonvpn.android"("5.16.14.0"))

    execute {
        GetLongDelayFingerprint.method.returnEarly(0)
        GetShortDelayFingerprint.method.returnEarly(0)
    }
}