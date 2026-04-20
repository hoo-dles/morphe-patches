package hoodles.morphe.extension.soundcloud.feature;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class FeaturePatch {
    private static final Map<String, Boolean> replacements = new LinkedHashMap<>();
    static {
        replacements.put("offline_sync", true);
        replacements.put("no_audio_ads", true);
        replacements.put("hq_audio", true);
    }

    public static boolean getFeatureEnabled(String featureName, boolean defaultEnabled) {
        if (replacements.containsKey(featureName))
            return Boolean.TRUE.equals(replacements.get(featureName));

        return defaultEnabled;
    }
}