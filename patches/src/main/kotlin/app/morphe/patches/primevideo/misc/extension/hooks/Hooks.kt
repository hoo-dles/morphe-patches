package app.morphe.patches.primevideo.misc.extension.hooks

import app.morphe.patches.shared.misc.extension.activityOnCreateExtensionHook

internal val applicationInitHook = activityOnCreateExtensionHook(
    "/SplashScreenActivity;"
)
