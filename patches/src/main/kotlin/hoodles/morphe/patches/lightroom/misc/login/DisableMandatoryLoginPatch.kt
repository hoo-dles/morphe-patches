package hoodles.morphe.patches.lightroom.misc.login

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val disableMandatoryLoginPatch = bytecodePatch(
    name = "Disable mandatory login",
    description = "Disables the mandatory login requirement, allowing the app to be used without signing in."
) {
    compatibleWith(Compatibility(
        name = "Lightroom",
        packageName = "com.adobe.lrmobile",
        appIconColor = 0x31A8FF,
        targets = listOf(AppTarget("9.3.0"))
    ))

    execute {
        // Set isLoggedIn = true.
        IsLoggedInMethodFingerprint.method.returnEarly(true)
    }
}
