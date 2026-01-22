package app.morphe.patches.primevideo.misc.extension

import app.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch(
    "primevideo",
    activityOnCreateExtensionHook("/SplashScreenActivity;")
)
