package app.morphe.patches.materialcapsule.pro

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.pairip.license.disableLicenseCheckPatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith("com.pryshedko.mtisland"("10.3"))

    dependsOn(disableLicenseCheckPatch)

    execute {
        DecryptFingerprint.method.returnEarly("yes")
    }
}