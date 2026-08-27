/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.pairip.resources

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.rawResourcePatch
import java.util.function.Supplier
import kotlin.io.path.deleteIfExists

internal val mergeDexPatch = bytecodePatch {
    dependsOn(extractDexPatch)

    extendWithAll { extractedDexStreams.map { Supplier { it } } }
}

internal val pairipResourcesPatch = rawResourcePatch {
    // extract then merge embedded DEX files from assets
    dependsOn(mergeDexPatch)

    execute {
        // delete bytecode assets
        for (asset in pairipAssets)
            asset.toPath().deleteIfExists()
    }
}