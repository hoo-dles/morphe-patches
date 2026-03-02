package app.morphe.patches.pandora.misc

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enableUnlimitedSkipsPatch = bytecodePatch(
    name = "Unlimited skips",
    description = "Disables the limit for skipping songs during playback."
) {
    compatibleWith("com.pandora.android")

    execute {
        SkipLimitBehaviorFingerprint.method.returnEarly("unlimited")
    }
}
