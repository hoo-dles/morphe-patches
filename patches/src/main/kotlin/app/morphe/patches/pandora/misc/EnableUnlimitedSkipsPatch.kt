package app.morphe.patches.pandora.misc

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enableUnlimitedSkipsPatch = bytecodePatch(
    name = "Enable unlimited skips",
) {
    compatibleWith("com.pandora.android")

    execute {
        SkipLimitBehaviorFingerprint.method.returnEarly("unlimited")
    }
}
