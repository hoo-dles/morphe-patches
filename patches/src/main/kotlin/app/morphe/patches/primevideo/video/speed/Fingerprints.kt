package app.morphe.patches.primevideo.video.speed

import app.morphe.patcher.Fingerprint


internal object PlaybackFeatureV2ResourceManagerPrepareFingerprint : Fingerprint(
    definingClass = "Lcom/amazon/avod/aavpui/feature/PlaybackFeatureV2ResourceManager;",
    name = "prepareForPlayback",
)
internal object PlaybackFeatureV2ResourceManagerResetFingerprint : Fingerprint(
    definingClass = "Lcom/amazon/avod/aavpui/feature/PlaybackFeatureV2ResourceManager;",
    name = "reset",
)