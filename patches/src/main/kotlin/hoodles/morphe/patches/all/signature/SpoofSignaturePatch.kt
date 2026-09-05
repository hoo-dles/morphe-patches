/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.all.signature

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patches.all.misc.extension.sharedExtensionPatch
import app.morphe.util.getNode
import app.morphe.util.writeRegister
import hoodles.morphe.patches.all.signature.Constants.SPOOF_CLASS_SMALI_NAME
import hoodles.morphe.util.getEndEntityCertificate
import hoodles.morphe.util.isCertMaybeInauthentic
import org.w3c.dom.Element
import java.util.Base64

private lateinit var packageName: String
private lateinit var signature: String

private val manifestPatch = resourcePatch {
    execute {
        val cert = getEndEntityCertificate(packageMetadata.signingCertificates)

        if (isCertMaybeInauthentic(cert)) throw PatchException("Invalid signing certificate. Original APK is required.")

        signature = Base64.getEncoder().encodeToString(cert.encoded)
        packageName = packageMetadata.packageName

        document("AndroidManifest.xml").use { document ->
            val application = document.getNode("application") as Element
            val applicationClass = application.getAttribute("android:name")
            if (applicationClass.isEmpty())
                application.setAttribute("android:name", Constants.SPOOF_CLASS_JAVA_NAME)
        }
    }
}

val spoofSignaturePatch = bytecodePatch(
    name = "Spoof signature",
    description = "Spoofs the package signature of the original APK.",
    default = false
) {
    dependsOn(manifestPatch, sharedExtensionPatch("common/signature"))

    finalize {
        SignatureSpoofApplicationCtorFingerprint.apply {
            val strippedSig = signature.filter { !it.isWhitespace() }

            instructionMatches.first().also {
                method.replaceInstruction(
                    it.index,
                    """const-string v${it.instruction.writeRegister}, "$packageName""""
                )
            }

            instructionMatches.last().also {
                method.replaceInstruction(
                    it.index,
                    """const-string v${it.instruction.writeRegister}, "$strippedSig""""
                )
            }

            // If Application subclass chain exists, insert our spoof class right before
            // android.app.Application
            mutableClassDefByOrNull {
                it.type != SPOOF_CLASS_SMALI_NAME && it.superclass == "Landroid/app/Application;"
            }?.setSuperClass(SPOOF_CLASS_SMALI_NAME)
        }
    }
}