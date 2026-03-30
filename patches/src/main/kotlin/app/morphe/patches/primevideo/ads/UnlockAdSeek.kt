package app.morphe.extension.primevideo.ads


object UnlockAdSeekPatch {
    @JvmStatic
    fun disableShowAds(contentPresentation: Any) {
        try {
            val contentConfig = contentPresentation::class.java
                .getDeclaredField("currentContentConfig")
                .apply { isAccessible = true }
                .get(contentPresentation) ?: return

            contentConfig::class.java
                .getDeclaredField("showAds")
                .apply { isAccessible = true }
                .setBoolean(contentConfig, false)

        } catch (e: Exception) {
        }
    }
}
