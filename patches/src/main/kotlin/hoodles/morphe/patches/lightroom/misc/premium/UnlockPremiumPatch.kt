package hoodles.morphe.patches.lightroom.misc.premium

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch

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
        // Set hasPremium = true.
        HasPurchasedMethodFingerprint.method.replaceInstruction(2, "const/4 v2, 0x1")
    }
}
