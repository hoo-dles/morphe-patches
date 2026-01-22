package app.morphe.patches.teuida.misc.extension

import app.morphe.patches.teuida.misc.extension.hooks.startActivityInitHook
import app.morphe.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionsPatch = sharedExtensionPatch(startActivityInitHook)
