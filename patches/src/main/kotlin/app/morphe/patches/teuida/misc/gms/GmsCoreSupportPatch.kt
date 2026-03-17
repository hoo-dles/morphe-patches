package app.morphe.patches.teuida.misc.gms

import app.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.shared.misc.extension.sharedExtensionPatch
import app.morphe.patches.shared.misc.gms.getMainOnCreateFingerprint
import app.morphe.patches.shared.misc.gms.gmsCoreSupportPatch
import app.morphe.patches.shared.misc.gms.gmsCoreSupportResourcePatch
import app.morphe.patches.teuida.misc.gms.Constants.MAIN_ACTIVITY_NAME
import app.morphe.patches.teuida.misc.gms.Constants.MORPHE_TEUIDA_PACKAGE_NAME
import app.morphe.patches.teuida.misc.gms.Constants.TEUIDA_PACKAGE_NAME

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    fromPackageName = TEUIDA_PACKAGE_NAME,
    toPackageName = MORPHE_TEUIDA_PACKAGE_NAME,
    mainActivityOnCreateFingerprint = getMainOnCreateFingerprint(MAIN_ACTIVITY_NAME),
    gmsCoreSupportResourcePatchFactory = ::gmsCoreSupportResourcePatch,
    extensionPatch = sharedExtensionPatch(
        activityOnCreateExtensionHook(MAIN_ACTIVITY_NAME)
    )
) {
    compatibleWith(TEUIDA_PACKAGE_NAME("1.21.16"))
}

private fun gmsCoreSupportResourcePatch() =
    gmsCoreSupportResourcePatch(
        fromPackageName = TEUIDA_PACKAGE_NAME,
        toPackageName = MORPHE_TEUIDA_PACKAGE_NAME,
        spoofedPackageSignature = "67bd96b0fff3989af9ed068ec9bd9bbe7583b03b",
)