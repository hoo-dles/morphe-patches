package app.morphe.patches.mimo.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.mimo.misc.signature.spoofSignatureHeaderPatch
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium"
) {
    compatibleWith("com.getmimo")

    dependsOn(spoofSignatureHeaderPatch)

    execute {
        IsActiveNowFingerprint.method.returnEarly(true)
        GetTypeFingerprint.method.addInstruction(0, """
            sget-object v0, Lcom/getmimo/core/model/inapp/Subscription${'$'}Type;->Pro:Lcom/getmimo/core/model/inapp/Subscription${'$'}Type;
            return-object v0
        """.trimIndent())
    }
}