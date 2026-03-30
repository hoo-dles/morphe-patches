/*
 * Copyright 2026 Morphe.
 * https://github.com/MorpheApp/morphe-patches
 *
 * See the included NOTICE file for GPLv3 §7(b) and §7(c) terms that apply to this code.
 */
package app.morphe.patches.primevideo.video.speed

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.primevideo.shared.Constants
import app.morphe.patches.primevideo.misc.extension.sharedExtensionPatch

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/morphe/extension/primevideo/videoplayer/PlaybackSpeedPatch;"

@Suppress("unused")
val playbackSpeedPatch = bytecodePatch(
    name = "Playback speed",
    description = "Adds playback speed controls to the video player.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    dependsOn(sharedExtensionPatch)

    execute {
        PlaybackFeatureV2ResourceManagerPrepareFingerprint.method.addInstructions(
            0,
            """
        invoke-static { p2 }, $EXTENSION_CLASS_DESCRIPTOR->onPrepareForPlayback(Ljava/lang/Object;)V
        """.trimIndent(),
        )

        PlaybackFeatureV2ResourceManagerResetFingerprint.method.addInstructions(
            0,
            """
        invoke-static {}, $EXTENSION_CLASS_DESCRIPTOR->onReset()V
        """.trimIndent(),
        )
    }
}