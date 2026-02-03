package app.morphe.patches.dailypay.misc.gms

import app.morphe.patches.dailypay.misc.gms.Constants.DAILYPAY_PACKAGE_NAME
import app.morphe.patches.dailypay.misc.gms.Constants.MORPHE_DAILYPAY_PACKAGE_NAME
import app.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.shared.misc.extension.sharedExtensionPatch
import app.morphe.patches.shared.misc.gms.gmsCoreSupportPatch
import app.morphe.patches.shared.misc.gms.gmsCoreSupportResourcePatch
import app.morphe.util.indexOfFirstInstruction
import com.android.tools.smali.dexlib2.Opcode

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    fromPackageName = DAILYPAY_PACKAGE_NAME,
    toPackageName = MORPHE_DAILYPAY_PACKAGE_NAME,
    mainActivityOnCreateFingerprint = MainActivityOnCreateFingerprint,
    insertIndexResolver = { method -> method.indexOfFirstInstruction(Opcode.INVOKE_SUPER) + 1},
    extensionPatch = sharedExtensionPatch(
        activityOnCreateExtensionHook(
            "/MainActivity;",
        )
    ),
    gmsCoreSupportResourcePatchFactory = ::gmsCoreSupportResourcePatch,
) {
    compatibleWith(DAILYPAY_PACKAGE_NAME)
}

private fun gmsCoreSupportResourcePatch() =
    gmsCoreSupportResourcePatch(
        fromPackageName = DAILYPAY_PACKAGE_NAME,
        toPackageName = MORPHE_DAILYPAY_PACKAGE_NAME,
        spoofedPackageSignature = "838ec2c270f8ca934e03b4894f4c15b6a4a4f3fd",
)