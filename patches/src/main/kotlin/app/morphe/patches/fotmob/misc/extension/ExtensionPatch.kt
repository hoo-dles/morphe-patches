package app.morphe.patches.fotmob.misc.extension

import app.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch(
    "fotmob",
    activityOnCreateExtensionHook("/MainActivityWrapper;")
)
