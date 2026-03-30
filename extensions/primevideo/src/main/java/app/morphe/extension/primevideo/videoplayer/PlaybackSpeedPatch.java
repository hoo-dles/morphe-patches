package app.morphe.extension.primevideo.videoplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

import com.amazon.video.sdk.player.Player;
import app.morphe.extension.shared.Logger;
import app.morphe.extension.shared.Utils;
import app.morphe.extension.shared.ui.Dim;

public class PlaybackSpeedPatch {
    private static Player player;
    private static volatile int currentSessionId = 0;

    private static final float[] SPEED_VALUES = {0.5f, 0.7f, 0.8f, 0.9f, 0.95f, 1.0f, 1.05f, 1.1f, 1.2f, 1.3f, 1.5f, 2.0f};
    private static final String SPEED_BUTTON_TAG = "speed_overlay";
    private static final String PREFS_NAME = "playback_speed_prefs";

    private static final int DEFAULT_TOP_PORT  = 48;
    private static final int DEFAULT_RIGHT_PORT = 0;
    private static final int DEFAULT_TOP_LAND  = 8;
    private static final int DEFAULT_RIGHT_LAND = 16;

    private static final String KEY_TOP_PORT   = "pos_top_portrait";
    private static final String KEY_RIGHT_PORT = "pos_right_portrait";
    private static final String KEY_TOP_LAND   = "pos_top_landscape";
    private static final String KEY_RIGHT_LAND = "pos_right_landscape";

    private static boolean isDialogShowing = false;

    public static void onPrepareForPlayback(Object globalResourceContext) {
        try {
            if (globalResourceContext == null) {
                Logger.printInfo(() -> "globalResourceContext is null, skipping");
                return;
            }
            Logger.printInfo(() -> "onPrepareForPlayback called");
            final int sessionId = ++currentSessionId;

            Field playerField = globalResourceContext.getClass().getDeclaredField("player");
            playerField.setAccessible(true);
            Object playerObj = playerField.get(globalResourceContext);

            if (playerObj instanceof Player) {
                player = (Player) playerObj;
                Logger.printInfo(() -> "Player found");
                Utils.runOnMainThread(() -> showSpeedButton(sessionId));
            } else {
                Logger.printInfo(() -> "player field is not Player instance: "
                        + (playerObj != null ? playerObj.getClass().getName() : "null"));
            }
        } catch (Exception e) {
            Logger.printException(() -> "onPrepareForPlayback error", e);
        }
    }

    public static void onReset() {
        final int resetSessionId = currentSessionId;
        player = null;
        Utils.runOnMainThread(() -> {
            if (resetSessionId != currentSessionId) {
                Logger.printInfo(() -> "onReset: new session already started, skip hide");
                return;
            }
            hideSpeedButton();
        });
    }

    private static void showSpeedButton(int sessionId) {
        try {
            if (sessionId != currentSessionId) return;

            Activity activity = getActivity();
            if (activity == null) {
                Logger.printException(() -> "Activity is null");
                return;
            }

            ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
            View old = decor.findViewWithTag(SPEED_BUTTON_TAG);
            if (old != null) decor.removeView(old);

            ImageView speedButton = createSpeedButton(decor.getContext());

            speedButton.setOnClickListener(v -> {
                Activity act = getActivity();
                if (act != null) changePlaybackSpeed(act);
            });
            speedButton.setOnTouchListener(new DragTouchListener(decor, speedButton)); // ← 追加

            decor.addView(speedButton, new FrameLayout.LayoutParams(Dim.dp48, Dim.dp48));

            speedButton.post(() -> restorePosition(decor.getContext(), speedButton));

            decor.addOnLayoutChangeListener((v, l, t, r, b, ol, ot, or, ob) -> {
                View btn = decor.findViewWithTag(SPEED_BUTTON_TAG);
                if (btn != null) btn.post(() -> restorePosition(decor.getContext(), btn));
            });

            Logger.printInfo(() -> "Speed button added (session=" + sessionId + ")");

        } catch (Exception e) {
            Logger.printException(() -> "showSpeedButton error", e);
        }
    }


    private static boolean isLandscape(Context ctx) {
        return ctx.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    private static FrameLayout.LayoutParams loadLayoutParams(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Dim.dp48, Dim.dp48);
        params.gravity = Gravity.TOP | Gravity.END;

        if (isLandscape(ctx)) {
            params.topMargin   = prefs.getInt(KEY_TOP_LAND,   dpToPx(ctx, DEFAULT_TOP_LAND));
            params.rightMargin = prefs.getInt(KEY_RIGHT_LAND, dpToPx(ctx, DEFAULT_RIGHT_LAND));
        } else {
            params.topMargin   = prefs.getInt(KEY_TOP_PORT,   dpToPx(ctx, DEFAULT_TOP_PORT));
            params.rightMargin = prefs.getInt(KEY_RIGHT_PORT, dpToPx(ctx, DEFAULT_RIGHT_PORT));
        }
        return params;
    }

    private static final String KEY_X_PORT = "pos_x_portrait";
    private static final String KEY_Y_PORT = "pos_y_portrait";
    private static final String KEY_X_LAND = "pos_x_landscape";
    private static final String KEY_Y_LAND = "pos_y_landscape";

    private static void savePosition(Context ctx, float x, float y) {
        SharedPreferences.Editor ed = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        if (isLandscape(ctx)) {
            ed.putFloat(KEY_X_LAND, x);
            ed.putFloat(KEY_Y_LAND, y);
        } else {
            ed.putFloat(KEY_X_PORT, x);
            ed.putFloat(KEY_Y_PORT, y);
        }
        ed.apply();
    }

    private static void restorePosition(Context ctx, View btn) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (isLandscape(ctx)) {
            float defX = dpToPx(ctx, DEFAULT_RIGHT_LAND);
            float defY = dpToPx(ctx, DEFAULT_TOP_LAND);
            btn.setX(prefs.getFloat(KEY_X_LAND, defX));
            btn.setY(prefs.getFloat(KEY_Y_LAND, defY));
        } else {
            float defX = dpToPx(ctx, DEFAULT_RIGHT_PORT);
            float defY = dpToPx(ctx, DEFAULT_TOP_PORT);
            btn.setX(prefs.getFloat(KEY_X_PORT, defX));
            btn.setY(prefs.getFloat(KEY_Y_PORT, defY));
        }
    }

    private static int dpToPx(Context ctx, int dp) {
        return Math.round(dp * ctx.getResources().getDisplayMetrics().density);
    }


    private static class DragTouchListener implements View.OnTouchListener {
        private final ViewGroup decor;
        private ImageView button = null;

        private static final long LONG_PRESS_TIMEOUT = 500L;
        private boolean isDragging = false;
        private float offsetX, offsetY;
        private final Handler handler = new Handler(Looper.getMainLooper());

        private final Runnable startDrag = () -> {
            isDragging = true;
            button.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).start();
            button.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        };

        private float clamp(float value, float min, float max) {
            return Math.max(min, Math.min(value, max));
        }

        DragTouchListener(ViewGroup decor, ImageView button) {
            this.decor = decor;
            this.button = button;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    handler.postDelayed(startDrag, LONG_PRESS_TIMEOUT);
                    offsetX = event.getRawX() - v.getX();
                    offsetY = event.getRawY() - v.getY();
                    return false;

                case MotionEvent.ACTION_MOVE:
                    if (isDragging) {
                        float newX = clamp(event.getRawX() - offsetX, 0, decor.getWidth()  - v.getWidth());
                        float newY = clamp(event.getRawY() - offsetY, 0, decor.getHeight() - v.getHeight());
                        v.setX(newX);
                        v.setY(newY);
                        return true;
                    }
                    return false;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    handler.removeCallbacks(startDrag);
                    if (isDragging) {
                        isDragging = false;
                        button.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                        savePosition(button.getContext(), button.getX(), button.getY());
                        Toast.makeText(button.getContext(), "Save Location", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                        v.performClick();
                    }
                    return true;
            }
            return false;
        }
    }

    private static void changePlaybackSpeed(Context context) {
        if (isDialogShowing) return;
        isDialogShowing = true;
        if (player == null) {
            Logger.printException(() -> "Player not available");
            return;
        }
        try {
            player.getClass().getMethod("pause").invoke(player);

            String[] options = new String[SPEED_VALUES.length];
            for (int i = 0; i < SPEED_VALUES.length; i++) options[i] = SPEED_VALUES[i] + "x";

            int currentIndex = 5;
            try {
                float rate = player.getPlaybackRate();
                int idx = Arrays.binarySearch(SPEED_VALUES, rate);
                if (idx >= 0) currentIndex = idx;
            } catch (Exception ignored) {}

            final boolean[] speedSelected = {false};

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Select Playback Speed")
                    .setSingleChoiceItems(options, currentIndex, (d, which) -> {
                        try {
                            player.setPlaybackRate(SPEED_VALUES[which]);
                            speedSelected[0] = true;
                            player.getClass().getMethod("play").invoke(player);
                        } catch (Exception e) {
                            Logger.printException(() -> "setPlaybackRate error", e);
                        } finally {
                            d.dismiss();
                        }
                    })
                    .create();

            dialog.setOnDismissListener(d -> {
                isDialogShowing = false;
                if (!speedSelected[0]) {
                    try {
                        player.getClass().getMethod("play").invoke(player);
                    } catch (Exception e) {
                        Logger.printException(() -> "play on dismiss error", e);
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            Logger.printException(() -> "changePlaybackSpeed error", e);
        }
    }
    private static Activity getActivity() {
        try {
            Class<?> atClass = Class.forName("android.app.ActivityThread");
            Object at = atClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = atClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map<?, ?> activities = (Map<?, ?>) activitiesField.get(at);
            for (Object record : activities.values()) {
                Field actField = record.getClass().getDeclaredField("activity");
                actField.setAccessible(true);
                Object act = actField.get(record);
                if (act instanceof Activity) return (Activity) act;
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
        speedButton.setLongClickable(false);
        speedButton.setScaleType(ImageView.ScaleType.CENTER);
        speedButton.setImageDrawable(new SpeedIconDrawable());
        speedButton.setMinimumWidth(Dim.dp48);
        speedButton.setMinimumHeight(Dim.dp48);
        return speedButton;
    }

    private static void hideSpeedButton() {
        try {
            Activity activity = getActivity();
            if (activity == null) return;
            ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
            View btn = decor.findViewWithTag(SPEED_BUTTON_TAG);
            if (btn != null) decor.removeView(btn);
            Logger.printInfo(() -> "Speed button removed");
        } catch (Exception e) {
            Logger.printException(() -> "hideSpeedButton error", e);
        }
    }

    static class SpeedIconDrawable extends Drawable {
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
}