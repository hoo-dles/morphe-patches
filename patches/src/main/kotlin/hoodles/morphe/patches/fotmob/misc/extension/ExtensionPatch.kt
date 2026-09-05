/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.fotmob.misc.extension

import app.morphe.patches.all.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.all.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch(
    "fotmob",
    activityOnCreateExtensionHook("/MainActivityWrapper;")
)
