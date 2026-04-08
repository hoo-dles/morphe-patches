package app.morphe.patches.primevideo.speed

import app.morphe.patcher.Fingerprint

object IsPlaybackSettingsV2EnabledFingerprint : Fingerprint(
    definingClass = "Lcom/amazon/video/sdk/stores/overlays/settings/PlaybackSettingsV2Config;",
    name = "isEnabled"
)

object IsPlaybackSpeedFeatureEnabledFingerprint : Fingerprint(
    definingClass = "Lcom/amazon/video/sdk/stores/overlays/settings/features/playbackspeed/store/PlaybackSpeedFeatureConfig;",
    name = "isPlaybackSpeedFeatureEnabled"
)