package app.morphe.extension.primevideo.videoplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import java.lang.reflect.Field;
import java.util.Arrays;

import com.amazon.video.sdk.player.Player;
import app.morphe.extension.shared.Logger;
import app.morphe.extension.shared.Utils;
import app.morphe.extension.shared.ui.Dim;

public class PlaybackSpeedPatch {
    private static Class<?> playerClass;
    private static Player player;
    private static final float[] SPEED_VALUES = {0.5f, 0.7f, 0.8f, 0.9f, 0.95f, 1.0f, 1.05f, 1.1f, 1.2f, 1.3f, 1.5f, 2.0f};
    private static final String SPEED_BUTTON_TAG = "speed_overlay";

    public static void onPrepareForPlayback(Object globalResourceContext) {
        try {
            if (globalResourceContext == null) {
                Logger.printInfo(() -> "globalResourceContext is null, skipping");
                return;
            }
            Logger.printInfo(() -> "onPrepareForPlayback called");

            Field playerField = globalResourceContext.getClass().getDeclaredField("player");
            playerField.setAccessible(true);
            Object playerObj = playerField.get(globalResourceContext);

            if (playerObj instanceof Player) {
                player = (Player) playerObj;
                Logger.printInfo(() -> "Player found");
                Utils.runOnMainThread(PlaybackSpeedPatch::showSpeedButton);
            } else {
                Logger.printInfo(() -> "player field is not Player instance: "
                        + (playerObj != null ? playerObj.getClass().getName() : "null"));
            }
        } catch (Exception e) {
            Logger.printException(() -> "onPrepareForPlayback error", e);
        }
    }

    private static void showSpeedButton() {
        try {
            android.app.Activity activity = getActivity();
            if (activity == null) {
                Logger.printException(() -> "Activity is null");
                return;
            }

            ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();

            if (decor.findViewWithTag(SPEED_BUTTON_TAG) != null) {
                Logger.printInfo(() -> "Button already exists");
                return;
            }

            ImageView speedButton = createSpeedButton(decor.getContext());
            speedButton.setOnClickListener(v -> changePlaybackSpeed(speedButton));

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Dim.dp48, Dim.dp48);
            params.gravity = Gravity.TOP | Gravity.END;
            params.topMargin = Dim.dp48;

            decor.addView(speedButton, params);
            Logger.printInfo(() -> "Speed button added");

        } catch (Exception e) {
            Logger.printException(() -> "showSpeedButton error", e);
        }
    }

    private static android.app.Activity getActivity() {
        try {
            Class<?> atClass = Class.forName("android.app.ActivityThread");
            Object at = atClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = atClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            java.util.Map<?, ?> activities = (java.util.Map<?, ?>) activitiesField.get(at);
            for (Object record : activities.values()) {
                Field actField = record.getClass().getDeclaredField("activity");
                actField.setAccessible(true);
                Object act = actField.get(record);
                if (act instanceof android.app.Activity) {
                    return (android.app.Activity) act;
                }
            }
        } catch (Exception e) {
            Logger.printException(() -> "getActivity error", e);
        }
        return null;
    }

    private static ImageView createSpeedButton(Context context) {
        ImageView speedButton = new ImageView(context);
        speedButton.setTag(SPEED_BUTTON_TAG);
        speedButton.setClickable(true);
        speedButton.setFocusable(true);
        speedButton.setScaleType(ImageView.ScaleType.CENTER);
        speedButton.setImageDrawable(new SpeedIconDrawable());
        speedButton.setMinimumWidth(Dim.dp48);
        speedButton.setMinimumHeight(Dim.dp48);
        return speedButton;
    }

    private static void changePlaybackSpeed(ImageView imageView) {
        if (player == null) {
            Logger.printException(() -> "Player not available");
            return;
        }
        try {
            // pause/play はリフレクションで呼ぶ
            player.getClass().getMethod("pause").invoke(player);

            String[] options = new String[SPEED_VALUES.length];
            for (int i = 0; i < SPEED_VALUES.length; i++) options[i] = SPEED_VALUES[i] + "x";

            int currentIndex = 5;
            try {
                float rate = player.getPlaybackRate();
                int idx = Arrays.binarySearch(SPEED_VALUES, rate);
                if (idx >= 0) currentIndex = idx;
            } catch (Exception ignored) {}

            AlertDialog dialog = new AlertDialog.Builder(imageView.getContext())
                    .setTitle("Select Playback Speed")
                    .setSingleChoiceItems(options, currentIndex, (d, which) -> {
                        try {
                            player.setPlaybackRate(SPEED_VALUES[which]);
                            player.getClass().getMethod("play").invoke(player);
                        } catch (Exception e) {
                            Logger.printException(() -> "setPlaybackRate error", e);
                        } finally {
                            d.dismiss();
                        }
                    })
                    .create();
            dialog.setOnDismissListener(d -> {
                try {
                    player.getClass().getMethod("play").invoke(player);
                } catch (Exception e) {
                    Logger.printException(() -> "play error", e);
                }
            });
            dialog.show();
        } catch (Exception e) {
            Logger.printException(() -> "changePlaybackSpeed error", e);
        }
    }
    public static void onReset() {
        player = null;
        playerClass = null;
        Utils.runOnMainThread(PlaybackSpeedPatch::hideSpeedButton);
    }

    private static void hideSpeedButton() {
        try {
            android.app.Activity activity = getActivity();
            if (activity == null) return;
            ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
            android.view.View btn = decor.findViewWithTag(SPEED_BUTTON_TAG);
            if (btn != null) decor.removeView(btn);
            Logger.printInfo(() -> "Speed button removed");
        } catch (Exception e) {
            Logger.printException(() -> "hideSpeedButton error", e);
        }
    }
}

class SpeedIconDrawable extends Drawable {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void draw(Canvas canvas) {
        int w = getBounds().width(), h = getBounds().height();
        float cx = w / 2f, cy = h * 0.7f;
        float radius = Math.min(w, h) / 2f * 0.8f;

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius * 0.1f);
        canvas.drawArc(new RectF(cx - radius, cy - radius, cx + radius, cy + radius), 180, 180, false, paint);

        paint.setStrokeWidth(radius * 0.06f);
        for (int i = 0; i < 3; i++) {
            float rad = (float) Math.toRadians(180 + i * 45);
            canvas.drawLine(cx + radius * 0.8f * (float) Math.cos(rad), cy + radius * 0.8f * (float) Math.sin(rad),
                    cx + radius * (float) Math.cos(rad), cy + radius * (float) Math.sin(rad), paint);
        }

        paint.setStrokeWidth(radius * 0.08f);
        float nr = (float) Math.toRadians(200);
        canvas.drawLine(cx, cy, cx + radius * 0.6f * (float) Math.cos(nr), cy + radius * 0.6f * (float) Math.sin(nr), paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, radius * 0.06f, paint);
    }

    @Override public void setAlpha(int alpha) { paint.setAlpha(alpha); }
    @Override public void setColorFilter(ColorFilter cf) { paint.setColorFilter(cf); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
    @Override public int getIntrinsicWidth() { return Dim.dp32; }
    @Override public int getIntrinsicHeight() { return Dim.dp32; }
}