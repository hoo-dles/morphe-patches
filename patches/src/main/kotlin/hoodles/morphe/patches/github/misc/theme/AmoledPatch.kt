package hoodles.morphe.patches.github.misc.theme

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.util.addInstructionsAtControlFlowLabel
import app.morphe.util.findElementByAttributeValue
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c

val bytecodeOverrides = bytecodePatch {
    execute {
        val blackLong = "0xFF00000000000000L"

        // If GitHub's theming colors change around, we'll need to be smarter about this. For now,
        // we'll just assume that the parameters are relatively static.
        //
        // backgroundSecondary, backgroundTertiary, gradientHeaderBackgroundStart
        FunctionalColorsCtorFingerprint.method.addInstructions(0, """
            const-wide p3, $blackLong
            const-wide p11, $blackLong
            const-wide p119, $blackLong
        """.trimIndent())

        // Override with window.setNavigationBarContrastEnforced(false)
        SetNavigationBarContrastFingerprint.matchAll().forEach {
            val setContrastIndex = it.instructionMatches.first().index
            val setContrastReg = it.method.getInstruction<Instruction35c>(setContrastIndex).registerD

            Opcode.INVOKE_VIRTUAL
            it.method.addInstructionsAtControlFlowLabel(setContrastIndex, """
                const/4 v$setContrastReg, 0x0
            """.trimIndent())
        }
    }
}

@Suppress("unused")
val amoledPatch = resourcePatch(
    name = "AMOLED dark theme",
    description = "Changes the default dark theme to use true blacks for AMOLED screens.",
    default = false
) {
    dependsOn(bytecodeOverrides)

    compatibleWith(Compatibility(
        name = "GitHub",
        packageName = "com.github.android",
        appIconColor = 0x000000,
        targets = listOf(AppTarget("1.255.0"))
    ))

    execute {
        val trueBlack = "#000000"

        // Theme colors
        document("res/values-night/colors.xml").use { document ->
            val blackColors = listOf(
                "backgroundInset",
                "backgroundSecondary",
                "gradientHeaderBackgroundStart"
            )

            val colors = document.getElementsByTagName("color")
            blackColors.forEach {
                colors.findElementByAttributeValue("name", it)?.textContent = trueBlack
            }
        }

        // Override alternate row styling for Markdown tables
        val mdCssFile = get("assets/webview/markdown.css", true)
        val css = mdCssFile.readText()
        val newCss = css.replace("--tr-alt-background", "--background")
        mdCssFile.writeText(newCss)
    }
}