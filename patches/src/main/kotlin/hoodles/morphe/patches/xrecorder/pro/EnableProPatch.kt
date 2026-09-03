/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.xrecorder.pro

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.misc.fix.changepackageinstaller.changePackageInstallerPatch
import app.morphe.util.getMutableMethod
import app.morphe.util.getReference
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import hoodles.morphe.patches.all.signature.spoofSignaturePatch
import hoodles.morphe.patches.xrecorder.shared.Constants

@Suppress("unused")
val enableProPatch = bytecodePatch(
    name = "Enable Pro"
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(spoofSignaturePatch, changePackageInstallerPatch())

    execute {
        GetProUsageFingerprint.instructionMatches.first()
            .getInstruction<ReferenceInstruction>()
            .getReference<MethodReference>()!!
            .getMutableMethod()
            .returnEarly(true)

        SetDisableAdsUsageFingerprint.apply {
            val defaultNoAdsIndex = instructionMatches.first().index
            val valueReg = instructionMatches.first().getInstruction<OneRegisterInstruction>().registerA

            method.replaceInstruction(defaultNoAdsIndex, """
                const/4 v${valueReg}, 0x1
            """.trimIndent())
        }
    }
}