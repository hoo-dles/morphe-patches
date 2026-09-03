/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.signature

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.util.getNode
import app.morphe.util.writeRegister
import hoodles.morphe.patches.shared.misc.extension.sharedExtensionPatch
import hoodles.morphe.patches.shared.misc.signature.Constants.SPOOF_CLASS_SMALI_NAME
import org.w3c.dom.Element

private val manifestPatch = resourcePatch {
    execute {
        document("AndroidManifest.xml").use { document ->
            val application = document.getNode("application") as Element
            val applicationClass = application.getAttribute("android:name")
            if (applicationClass.isEmpty())
                application.setAttribute("android:name", Constants.SPOOF_CLASS_JAVA_NAME)
        }
    }
}

fun spoofSignaturePatch(packageName: String, signature: String) = bytecodePatch {
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