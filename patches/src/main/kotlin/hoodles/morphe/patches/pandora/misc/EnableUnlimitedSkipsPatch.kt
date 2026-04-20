package hoodles.morphe.patches.pandora.misc

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.pandora.shared.Constants

@Suppress("unused")
val enableUnlimitedSkipsPatch = bytecodePatch(
    name = "Unlimited skips",
    description = "Disables the limit for skipping songs during playback."
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        SkipLimitBehaviorFingerprint.method.returnEarly("unlimited")
    }
}
