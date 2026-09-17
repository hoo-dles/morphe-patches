/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.all.signature

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.util.proxy.mutableTypes.encodedValue.MutableStringEncodedValue
import app.morphe.patches.all.misc.extension.sharedExtensionPatch
import app.morphe.util.getReference
import app.morphe.util.matchAllMethodIndicesForEach
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import hoodles.morphe.patches.all.signature.Constants.SPOOF_CLASS_SMALI_NAME
import hoodles.morphe.util.getEndEntityCertificate
import hoodles.morphe.util.isCertMaybeInauthentic
import java.util.Base64

private lateinit var packageName: String
private lateinit var signature: String

private val manifestPatch = resourcePatch {
    execute {
        val cert = getEndEntityCertificate(packageMetadata.signingCertificates)

        if (isCertMaybeInauthentic(cert)) throw PatchException("Invalid signing certificate. Original APK is required.")

        signature = Base64.getEncoder().encodeToString(cert.encoded)
        packageName = packageMetadata.packageName
    }
}

val spoofSignaturePatch = bytecodePatch(
    name = "Spoof signature",
    description = "Spoofs the package signature of the original APK.",
    default = false
) {
    dependsOn(manifestPatch, sharedExtensionPatch("all/signature"))

    execute {
        GetPackageInfoFingerprint.matchAllMethodIndicesForEach(false) { index ->
            val instr = getInstruction<Instruction35c>(index)
            val params = "Landroid/content/pm/PackageManager;" +
                    instr.getReference<MethodReference>()!!.parameterTypes.joinToString("")

            replaceInstruction(index,
                "invoke-static {v${instr.registerC}, v${instr.registerD}, v${instr.registerE}}, $SPOOF_CLASS_SMALI_NAME->getPackageInfo($params)Landroid/content/pm/PackageInfo;"
            )
        }
    }

    finalize {
        mutableClassDefBy(SPOOF_CLASS_SMALI_NAME).apply {
            (staticFields.first { it.name == "PACKAGE_NAME" }.initialValue as MutableStringEncodedValue).value = packageName
            (staticFields.first { it.name == "SIGNATURE" }.initialValue as MutableStringEncodedValue).value = signature
        }
    }
}