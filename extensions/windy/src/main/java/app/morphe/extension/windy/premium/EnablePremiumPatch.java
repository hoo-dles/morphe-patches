package app.morphe.extension.windy.premium;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import app.morphe.extension.shared.Logger;

@SuppressWarnings("unused")
public class EnablePremiumPatch {
    private static final Map<Pattern, String> replacements = new LinkedHashMap<>() {{
        // Patch `hasAny()` function
        put(Pattern.compile("null!==\\w+\\.get\\(\"subscription\"\\),"), "true,");
        // Patch logic run when null subscription is set
        put(Pattern.compile("\\w+\\.set\\(\"detail1h\",!1\\),"), "");
        put(Pattern.compile("set\\(\"subscription\",null\\),"), "set(\"subscription\",\"premium\"),");
        put(Pattern.compile("\\w+&&document\\.body\\.classList\\.remove\\(\"subs-\"\\.concat\\(\\w+\\)\\),"), "document.body.classList.add(\"subs-premium\"),");
    }};

    public static void patchAppJavascript(WebResourceRequest request, WebResourceResponse response) {
        if (request.getUrl().getPath() != null && request.getUrl().getPath().endsWith("mobile.js")) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getData()));
                StringBuilder contentBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    contentBuilder.append(line).append("\n");
                }
                String content = contentBuilder.toString();

                for (Map.Entry<Pattern, String> entry : replacements.entrySet()) {
                    content = entry.getKey().matcher(content).replaceAll(entry.getValue());
                }

                response.setData(new ByteArrayInputStream(content.getBytes()));
            } catch (Exception e) {
                Logger.printException(() -> "Failed patching original Javascript", e);
            }
        }
    }
}