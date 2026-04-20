package hoodles.morphe.patches.macrofactor.misc.signature

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.patches.macrofactor.shared.Constants
import hoodles.morphe.util.byteArrayOf

val SIGNATURE = byteArrayOf("72 80 3C E3 0B 7F 47 E8 4D 86 98 7F 45 1D AF 8C 7A CC B5 A0")
val WORKOUTS_SIGNATURE = byteArrayOf("44 40 CF FB DA 84 8C 6C 8B 9E 02 84 79 50 74 58 79 9D 51 2C")

val spoofSignaturePatch = bytecodePatch(
    name = "Spoof signature",
    description = "Spoof package signature for Firebase app check."
) {
    compatibleWith(*Constants.COMPATIBILITY)

    execute {
        var signature = if (packageMetadata.packageName == Constants.PACKAGE_NAME)
            SIGNATURE else WORKOUTS_SIGNATURE

        GetSignatureFingerprint.method.apply {
            addInstructions(0, """
                const/16 v0, ${signature.size}
                new-array v0, v0, [B
                fill-array-data v0, :array_sig
                return-object v0

                :array_sig
                .array-data 1
                    ${signature.joinToString("\n") { "${it}t" }}
                .end array-data
            """.trimIndent())
        }
    }

}