package app.morphe.patches.guessthecountry.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val EnablePremiumPatch = bytecodePatch(
    name = "Enable Premium"
) {
    compatibleWith("com.qbis.guessthecountry")

    execute {
        IsProductInCacheFingerprint.method.returnEarly(true)
    }
}