package hoodles.morphe.patches.primevideo.misc.extension

import hoodles.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook
import hoodles.morphe.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch(
    "primevideo",
    activityOnCreateExtensionHook("/SplashScreenActivity;")
)
