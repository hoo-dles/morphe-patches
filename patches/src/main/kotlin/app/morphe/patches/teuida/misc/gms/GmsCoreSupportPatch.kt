package app.morphe.patches.teuida.misc.gms

import app.morphe.patcher.patch.Option
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.misc.gms.gmsCoreSupportPatch
import app.morphe.patches.teuida.misc.gms.Constants.MORPHE_TEUIDA_PACKAGE_NAME
import app.morphe.patches.teuida.misc.gms.Constants.TEUIDA_PACKAGE_NAME
import app.morphe.patches.shared.misc.gms.gmsCoreSupportResourcePatch
import app.morphe.patches.teuida.misc.extension.sharedExtensionsPatch
import app.morphe.util.returnEarly

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    fromPackageName = TEUIDA_PACKAGE_NAME,
    toPackageName = MORPHE_TEUIDA_PACKAGE_NAME,
    mainActivityOnCreateFingerprint = MainActivityOnCreateFingerprint,
    extensionPatch = sharedExtensionsPatch,
    gmsCoreSupportResourcePatchFactory = ::gmsCoreSupportResourcePatch,
) {
    compatibleWith(TEUIDA_PACKAGE_NAME)

    dependsOn(
        bytecodePatch {
            execute {
                IsGooglePlayServicesAvailableFingerprint.method.returnEarly()
            }
        }
    )
}

private fun gmsCoreSupportResourcePatch() =
    gmsCoreSupportResourcePatch(
        fromPackageName = TEUIDA_PACKAGE_NAME,
        toPackageName = MORPHE_TEUIDA_PACKAGE_NAME,
        spoofedPackageSignature = "67bd96b0fff3989af9ed068ec9bd9bbe7583b03b",
)