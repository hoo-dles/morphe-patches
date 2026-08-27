/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.pairip

import app.morphe.patcher.patch.RawResourcePatch
import app.morphe.patcher.patch.rawResourcePatch
import hoodles.morphe.patches.shared.misc.pairip.bytecode.getBytecodePatch
import hoodles.morphe.patches.shared.misc.pairip.extension.getExtensionPatch
import hoodles.morphe.patches.shared.misc.pairip.native.getNativeLibsPatch
import hoodles.morphe.patches.shared.misc.pairip.resources.pairipResourcesPatch

fun getStripPairipPatch(appName: String): RawResourcePatch = rawResourcePatch {
    dependsOn(
        pairipResourcesPatch,
        getNativeLibsPatch(appName),
        getBytecodePatch(appName),
        getExtensionPatch(appName)
    )
}