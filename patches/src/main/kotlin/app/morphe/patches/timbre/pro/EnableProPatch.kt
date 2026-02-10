package app.morphe.patches.timbre.pro

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
) {
    compatibleWith("xeus.timbre")

    execute {
        IsUserAProFingerprint.method.returnEarly(true)
    }
}