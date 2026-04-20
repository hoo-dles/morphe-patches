package hoodles.morphe.patches.fotmob.misc.extension

import hoodles.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import hoodles.morphe.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch(
    "fotmob",
    activityOnCreateExtensionHook("/MainActivityWrapper;")
)
