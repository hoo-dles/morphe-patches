/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.pairip

import app.morphe.patcher.patch.ApkArchitecture
import app.morphe.patcher.patch.BytecodePatch
import app.morphe.patcher.patch.PatchAvailability
import app.morphe.patcher.patch.bytecodePatch
import hoodles.morphe.patches.shared.misc.pairip.bytecode.getBytecodePatch
import hoodles.morphe.patches.shared.misc.pairip.extension.getExtensionPatch
import hoodles.morphe.patches.shared.misc.pairip.native.getNativeLibsPatch
import hoodles.morphe.patches.shared.misc.pairip.resources.pairipResourcesPatch

fun getStripPairipPatch(appName: String): BytecodePatch = bytecodePatch {
    dependsOn(
        pairipResourcesPatch,
        getNativeLibsPatch(appName),
        getBytecodePatch(appName),
        getExtensionPatch(appName)
    )

    availability { _, architecture ->
        when (architecture) {
            ApkArchitecture.ARMEABI_V7A -> PatchAvailability.REQUIRED
            else -> PatchAvailability.UNAVAILABLE
        }
    }
}