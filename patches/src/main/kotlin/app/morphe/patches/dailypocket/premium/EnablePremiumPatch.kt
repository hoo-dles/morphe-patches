package app.morphe.patches.dailypocket.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.dailypocket.shared.Constants
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21t

private val premiumWidgetPatch = bytecodePatch {
    execute {
        WidgetConfigOnCreateFingerprint.apply {
            val ifIndex = instructionMatches.last().index
            val ifRegister = method.getInstruction<Instruction21t>(ifIndex).registerA
            method.addInstruction(
                ifIndex,
                "const/4 v$ifRegister, 0x0")
        }
    }
}

@Suppress("unused")
val enablePremiumPatch = rawResourcePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(premiumWidgetPatch)

    execute {
        var indexFile = get("assets/www/assets")
            .listFiles()!!
            .first { file -> file.name.startsWith("index") && file.extension == "js" }

        var patchedCode = indexFile.readText().replace(
            Regex("""await [a-zA-Z]+\.asyncNativeStorageGetItem\("isPayment"\)==="true""""),
            "true"
        )

        indexFile.writeText(patchedCode)
    }
}