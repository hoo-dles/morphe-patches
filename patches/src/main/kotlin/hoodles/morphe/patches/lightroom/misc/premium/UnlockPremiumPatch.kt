package hoodles.morphe.patches.lightroom.misc.premium

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks premium features by making the premium check always return true."
) {
    compatibleWith(Compatibility(
        name = "Lightroom",
        packageName = "com.adobe.lrmobile",
        appIconColor = 0x31A8FF,
        targets = listOf(AppTarget("9.3.0"))
    ))

    execute {
        // Set hasPurchased = true.
        HasPurchasedMethodFingerprint.method.returnEarly(true)
    }
}
