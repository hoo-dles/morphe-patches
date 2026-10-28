/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.alltrails.heatmap

import app.morphe.patcher.Fingerprint

internal const val ALLTRAILS_HEATMAP_PATH =
    "/wmts/static/heatmaps/all/years_1/{z}/{x}/{y}.png"

internal const val ALLTRAILS_HEATMAP_HOST_SUFFIX =
    ".alltrails.com/wmts/static/heatmaps/all/years_1/{z}/{x}/{y}.png"

internal const val DEFAULT_HEATMAP_TILE_URL_TEMPLATE =
    "https://tiles.example.ts.net/tiles/years_1/all/{z}/{x}/{y}.png"

internal object HeatmapTilePathFingerprint : Fingerprint(
    strings = listOf(ALLTRAILS_HEATMAP_PATH)
)

internal object HeatmapTileHostSuffixFingerprint : Fingerprint(
    strings = listOf(ALLTRAILS_HEATMAP_HOST_SUFFIX)
)
