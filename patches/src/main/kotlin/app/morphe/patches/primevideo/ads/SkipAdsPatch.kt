package app.morphe.patches.primevideo.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.primevideo.misc.extension.sharedExtensionPatch
import app.morphe.util.*
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

@Suppress("unused")
val skipAdsPatch = bytecodePatch(
    name = "Skip ads",
    description = "Automatically skips video stream ads.",
) {
    compatibleWith("com.amazon.avod.thirdpartyclient"("3.0.430.1747"))

    dependsOn(sharedExtensionPatch)

    // Skip all the logic in ServerInsertedAdBreakState.enter(), which plays all the ad clips in this
    // ad break. Instead, force the video player to seek over the entire break and reset the state machine.
    execute {
        // Force doTrigger() access to public so we can call it from our extension.
        DoTriggerFingerprint.method.accessFlags = AccessFlags.PUBLIC.value;

        EnterServerInsertedAdBreakStateFingerprint.method.apply {
            // Get register that stores VideoPlayer:
            //  invoke-virtual ->getPrimaryPlayer()
            //  move-result-object { playerRegister }
            val playerIndex = indexOfFirstInstructionOrThrow() {
                opcode == Opcode.INVOKE_VIRTUAL && getReference<MethodReference>()?.name == "getPrimaryPlayer"
            }
            val playerRegister = getInstruction<OneRegisterInstruction>(playerIndex + 1).registerA

            // Reuse the params from the original method:
            //  p0 = ServerInsertedAdBreakState
            //  p1 = AdBreakTrigger
            addInstructions(
                playerIndex + 2,
                """
                    invoke-static { p0, p1, v$playerRegister }, Lapp/revanced/extension/primevideo/ads/SkipAdsPatch;->enterServerInsertedAdBreakState(Lcom/amazon/avod/media/ads/internal/state/ServerInsertedAdBreakState;Lcom/amazon/avod/media/ads/internal/state/AdBreakTrigger;Lcom/amazon/avod/media/playback/VideoPlayer;)V
                    return-void
                """
            )
        }

        // Return early from these callbacks to prevent unwanted overlays from ad breaks.
        OnSeekBehindUnwatchedAdFingerprint.method.returnEarly()
        OnSeekPastUnwatchedAdFingerprint.method.returnEarly()
    }
}

