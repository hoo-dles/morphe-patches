package hoodles.morphe.patches.duolingo.misc.googlelogin

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.PatchException
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.compatibility.Compat
import hoodles.morphe.patches.all.microg.microGSupportPatch

/**
 * On Android 14+ androidx.credentials sends "Sign in with Google" through the system
 * Credential Manager. The system forwards it to the credential providers enabled in Settings,
 * which normally means the real Google Play Services. Play Services rejects the re-signed app,
 * and MicroG is never asked unless the user enabled it as a provider, so the sign-in sheet just
 * closes (NoCredentialException).
 *
 * Reporting the framework provider as unavailable makes androidx.credentials fall back to its
 * bundled Play Services provider. That provider binds directly to the GmsCore package, which
 * the MicroG integration renamed to app.revanced.android.gms, and MicroG spoofs the original
 * signature there.
 */
@Suppress("unused")
val fixGoogleLoginPatch = bytecodePatch(
    name = "Fix Google login",
    description = "Routes Google sign-in through MicroG instead of the system Credential Manager. " +
            "Requires MicroG RE.",
    default = false
) {
    compatibleWith(Compat.DUOLINGO)

    dependsOn(microGSupportPatch)

    execute {
        CredentialProviderFrameworkImplFingerprint.classDef.methods
            .firstOrNull {
                it.name == "isAvailableOnDevice" && it.returnType == "Z" && it.parameterTypes.isEmpty()
            }
            ?.returnEarly(false)
            ?: throw PatchException("Could not find CredentialProviderFrameworkImpl.isAvailableOnDevice()")

        // MicroG reports a GmsCore version above PRE_U_MIN_GMS_APK_VERSION, so androidx would use the
        // identitycredentials GetCredentialController. MicroG's reply there lacks the provider data
        // androidx expects ("No provider data returned"). Force the legacy Identity sign-in path
        // (GetSignInIntent / BeginSignIn), which MicroG serves via auth.api.identity.service.signin.
        PlayServicesGetCredentialFingerprint.let {
            val moveResultIndex = it.instructionMatches.last().index
            val reg = it.method.getInstruction<OneRegisterInstruction>(moveResultIndex).registerA
            it.method.addInstruction(moveResultIndex + 1, "const/4 v$reg, 0x0")
        }
    }
}
