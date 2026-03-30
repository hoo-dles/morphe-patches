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
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
    private static boolean isDialogShowing = false;
    private static volatile float pendingSpeed = 1.0f;

    private static final float[] SPEED_VALUES = {
            0.5f, 0.7f, 0.8f, 0.9f, 0.95f, 1.0f, 1.05f, 1.1f, 1.2f, 1.3f, 1.5f, 2.0f
    };
    private static final String SPEED_BUTTON_TAG = "speed_overlay";
    private static final String PREFS_NAME       = "playback_speed_prefs";
    private static final String KEY_SPEED        = "playback_speed";
    private static final String KEY_SAVE_ENABLED = "save_speed_enabled";
    private static final String KEY_X_PORT       = "pos_x_portrait";
    private static final String KEY_Y_PORT       = "pos_y_portrait";
    private static final String KEY_X_LAND       = "pos_x_landscape";
    private static final String KEY_Y_LAND       = "pos_y_landscape";

    // ─────────────────────────────────────────────
    // Entry points called from patch
    // ─────────────────────────────────────────────

    public static void onPrepareForPlayback(Object globalResourceContext) {
        try {
            if (globalResourceContext == null) {
                Logger.printInfo(() -> "globalResourceContext is null, skipping");
                return;
            }
            Logger.printInfo(() -> "onPrepareForPlayback called");
            final int sessionId = ++currentSessionId;
            pendingSpeed = 1.0f;

            Field playerField = globalResourceContext.getClass().getDeclaredField("player");
            playerField.setAccessible(true);
            Object playerObj = playerField.get(globalResourceContext);

            if (!(playerObj instanceof Player)) {
                Logger.printInfo(() -> "player field is not Player: "
                        + (playerObj != null ? playerObj.getClass().getName() : "null"));
                return;
            }

            player = (Player) playerObj;
            Logger.printInfo(() -> "Player found");
            applySavedSpeed();
            scheduleAddButton(sessionId, 0);

        } catch (Exception e) {
            Logger.printException(() -> "onPrepareForPlayback error", e);
        }
    }

    public static void onReset() {
        final int resetSession = currentSessionId;
        player = null;
        new Handler(Looper.getMainLooper()).post(() -> {
            if (resetSession != currentSessionId) return;
            removeButton();
        });
    }

    // ─────────────────────────────────────────────
    // Button lifecycle
    // ─────────────────────────────────────────────

    private static void scheduleAddButton(int sessionId, int attempt) {
        long delay = attempt == 0 ? 0 : 300;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (sessionId != currentSessionId) return;
            Activity act = getActivity();
            if (act == null) {
                if (attempt < 5) scheduleAddButton(sessionId, attempt + 1);
                return;
            }
            addButton(act, sessionId, attempt);
        }, delay);
    }

    private static void addButton(Activity activity, int sessionId, int attempt) {
        activity.runOnUiThread(() -> {
            try {
                if (sessionId != currentSessionId) return;

                ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();

                View old = decor.findViewWithTag(SPEED_BUTTON_TAG);
                if (old != null) decor.removeView(old);

                ImageView btn = createSpeedButton(activity);

                btn.setOnClickListener(v -> changePlaybackSpeed(activity));

                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(Dim.dp48, Dim.dp48);
                lp.gravity = Gravity.TOP | Gravity.START;
                decor.addView(btn, lp);
                btn.setElevation(Float.MAX_VALUE);
                btn.bringToFront();

                btn.setOnTouchListener(new DragTouchListener(decor, btn));

                btn.post(() -> restorePosition(activity, btn));

                btn.postDelayed(() -> {
                    boolean ok = decor.findViewWithTag(SPEED_BUTTON_TAG) != null;
                    Logger.printInfo(() -> "Button confirmed=" + ok + " attempt=" + attempt);
                    if (!ok && attempt < 5) scheduleAddButton(sessionId, attempt + 1);
                }, 200);

                Logger.printInfo(() -> "Speed button added session=" + sessionId);

            } catch (Exception e) {
                Logger.printException(() -> "addButton error", e);
                if (attempt < 5) scheduleAddButton(sessionId, attempt + 1);
            }
        });
    }
    private static void removeButton() {
        try {
            Activity act = getActivity();
            if (act == null) return;
            ViewGroup decor = (ViewGroup) act.getWindow().getDecorView();
            View btn = decor.findViewWithTag(SPEED_BUTTON_TAG);
            if (btn != null) {
                btn.setOnClickListener(null);
                btn.setOnTouchListener(null);
                decor.removeView(btn);
                Logger.printInfo(() -> "Speed button removed");
            }
        } catch (Exception e) {
            Logger.printException(() -> "removeButton error", e);
        }
    }

    // ─────────────────────────────────────────────
    // Position save / restore
    // ─────────────────────────────────────────────

    private static boolean isLandscape(Context ctx) {
        return ctx.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    private static void savePosition(Context ctx, float x, float y) {
        SharedPreferences.Editor ed =
                ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        if (isLandscape(ctx)) {
            ed.putFloat(KEY_X_LAND, x).putFloat(KEY_Y_LAND, y);
        } else {
            ed.putFloat(KEY_X_PORT, x).putFloat(KEY_Y_PORT, y);
        }
        ed.apply();
    }

    private static void restorePosition(Activity act, View btn) {
        try {
            android.graphics.Rect usable = new android.graphics.Rect();
            act.getWindow().getDecorView().getWindowVisibleDisplayFrame(usable);
            int dW = usable.width();
            int dH = usable.height();

            if (dW == 0 || dH == 0) {
                btn.postDelayed(() -> restorePosition(act, btn), 100);
                return;
            }

            SharedPreferences prefs =
                    act.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            float x, y;
            if (isLandscape(act)) {
                float defX = dW - btn.getWidth()  - dpToPx(act, 16);
                float defY = usable.top + dpToPx(act, 8);
                x = prefs.getFloat(KEY_X_LAND, defX);
                y = prefs.getFloat(KEY_Y_LAND, defY);
            } else {
                float defX = dW - btn.getWidth();
                float defY = usable.top + dpToPx(act, 48);
                x = prefs.getFloat(KEY_X_PORT, defX);
                y = prefs.getFloat(KEY_Y_PORT, defY);
            }

            x = clamp(x, 0, dW - btn.getWidth());
            y = clamp(y, usable.top, usable.bottom - btn.getHeight());
            btn.setX(x);
            btn.setY(y);
            float finalX = x;
            float finalY = y;
            Logger.printInfo(() -> "Position restored x=" + finalX + " y=" + finalY);
        } catch (Exception e) {
            Logger.printException(() -> "restorePosition error", e);
        }
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(val, max));
    }

    private static int dpToPx(Context ctx, int dp) {
        return Math.round(dp * ctx.getResources().getDisplayMetrics().density);
    }

    // ─────────────────────────────────────────────
    // Speed dialog
    // ─────────────────────────────────────────────

    private static void changePlaybackSpeed(Context context) {
        if (isDialogShowing) return;
        if (player == null) {
            Logger.printException(() -> "Player not available");
            return;
        }
        isDialogShowing = true;
        try {
            player.getClass().getMethod("pause").invoke(player);

            SharedPreferences prefs =
                    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            boolean saveEnabled = prefs.getBoolean(KEY_SAVE_ENABLED, false);

            String[] options = new String[SPEED_VALUES.length];
            for (int i = 0; i < SPEED_VALUES.length; i++) options[i] = SPEED_VALUES[i] + "x";

            int currentIndex = 5;
            int idx = Arrays.binarySearch(SPEED_VALUES, pendingSpeed);
            if (idx >= 0) currentIndex = idx;

            final boolean[] speedSelected = {false};

            CheckBox checkBox = new CheckBox(context);
            checkBox.setText("Save speed for next episode");
            checkBox.setChecked(saveEnabled);

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Playback Speed")
                    .setView(checkBox)
                    .setSingleChoiceItems(options, currentIndex, (d, which) -> {
                        try {
                            float speed = SPEED_VALUES[which];
                            player.setPlaybackRate(speed);
                            pendingSpeed = speed;
                            speedSelected[0] = true;

                            SharedPreferences.Editor ed = prefs.edit();
                            ed.putBoolean(KEY_SAVE_ENABLED, checkBox.isChecked());
                            if (checkBox.isChecked()) ed.putFloat(KEY_SPEED, speed);
                            ed.apply();

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
                hideSystemUI(context);
            });

            dialog.show();
            hideSystemUI(context);

        } catch (Exception e) {
            isDialogShowing = false;
            Logger.printException(() -> "changePlaybackSpeed error", e);
        }
    }

    // ─────────────────────────────────────────────
    // Drag listener
    // ─────────────────────────────────────────────
    private static class DragTouchListener implements View.OnTouchListener {
        private final ViewGroup decor;
        private ImageView button = null;
        private static final long LONG_PRESS_MS = 400L;
        private boolean isDragging = false;
        private float startRawX, startRawY;
        private float offsetX, offsetY;
        private static final float DRAG_THRESHOLD_DP = 8f;
        private final Handler handler = new Handler(Looper.getMainLooper());

        private final Runnable startDrag = () -> {
            isDragging = true;
            button.animate().scaleX(1.25f).scaleY(1.25f).setDuration(120).start();
            button.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        };

        DragTouchListener(ViewGroup decor, ImageView button) {
            this.decor  = decor;
            this.button = button;
        }

        private Rect getUsableRect() {
            Rect rect = new Rect();
            decor.getWindowVisibleDisplayFrame(rect);
            return rect;
        }

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getActionMasked()) {

                case MotionEvent.ACTION_DOWN:
                    isDragging = false;
                    startRawX = e.getRawX();
                    startRawY = e.getRawY();
                    offsetX = e.getRawX() - v.getX();
                    offsetY = e.getRawY() - v.getY();
                    handler.postDelayed(startDrag, LONG_PRESS_MS);
                    return false;

                case MotionEvent.ACTION_MOVE:
                    if (!isDragging) {
                        float dx = e.getRawX() - startRawX;
                        float dy = e.getRawY() - startRawY;
                        float threshold = DRAG_THRESHOLD_DP
                                * v.getContext().getResources().getDisplayMetrics().density;
                        if (Math.sqrt(dx * dx + dy * dy) > threshold) {
                            handler.removeCallbacks(startDrag);
                        }
                        return false;
                    }
                    Rect usable = getUsableRect();
                    float maxX = usable.width()  - v.getWidth();
                    float maxY = usable.height() - v.getHeight();
                    float newX = clamp(e.getRawX() - offsetX, 0, maxX);
                    float newY = clamp(e.getRawY() - offsetY - usable.top, 0, maxY);
                    v.setX(newX);
                    v.setY(newY + usable.top);
                    return true;

                case MotionEvent.ACTION_UP:
                    handler.removeCallbacks(startDrag);
                    if (isDragging) {
                        isDragging = false;
                        button.animate().scaleX(1f).scaleY(1f).setDuration(120).start();
                        savePosition(v.getContext(), v.getX(), v.getY());
                        Toast.makeText(v.getContext(), "Save Location", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    v.performClick();
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    handler.removeCallbacks(startDrag);
                    if (isDragging) {
                        isDragging = false;
                        button.animate().scaleX(1f).scaleY(1f).setDuration(120).start();
                    }
                    return false;
            }
            return false;
        }

        private float clamp(float val, float min, float max) {
            return Math.max(min, Math.min(val, max));
        }
    }
    // ─────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────

    private static ImageView createSpeedButton(Context context) {
        ImageView btn = new ImageView(context);
        btn.setTag(SPEED_BUTTON_TAG);
        btn.setClickable(true);
        btn.setFocusable(true);
        btn.setScaleType(ImageView.ScaleType.CENTER);
        btn.setImageDrawable(new SpeedIconDrawable());
        btn.setMinimumWidth(Dim.dp48);
        btn.setMinimumHeight(Dim.dp48);
        return btn;
    }

    private static Activity getActivity() {
        try {
            Class<?> at = Class.forName("android.app.ActivityThread");
            Object thread = at.getMethod("currentActivityThread").invoke(null);
            Field f = at.getDeclaredField("mActivities");
            f.setAccessible(true);
            Map<?, ?> map = (Map<?, ?>) f.get(thread);
            for (Object rec : map.values()) {
                Field af = rec.getClass().getDeclaredField("activity");
                af.setAccessible(true);
                Object act = af.get(rec);
                if (act instanceof Activity) return (Activity) act;
            }
        } catch (Exception e) {
            Logger.printException(() -> "getActivity error", e);
        }
        return null;
    }

    private static void applySavedSpeed() {
        try {
            Activity act = getActivity();
            if (act == null) return;
            SharedPreferences prefs =
                    act.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            if (!prefs.getBoolean(KEY_SAVE_ENABLED, false)) {
                pendingSpeed = 1.0f;
                Logger.printInfo(() -> "Save Speed OFF -> reset to 1.0");
                return;
            }
            float speed = prefs.getFloat(KEY_SPEED, 1.0f);
            pendingSpeed = speed;
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                try {
                    if (player != null) player.setPlaybackRate(speed);
                } catch (Exception e) {
                    Logger.printException(() -> "applySavedSpeed error", e);
                }
            }, 600);
        } catch (Exception e) {
            Logger.printException(() -> "applySavedSpeed error", e);
        }
    }

    @SuppressWarnings("deprecation")
    private static void hideSystemUI(Context context) {
        try {
            if (context instanceof Activity) {
                ((Activity) context).getWindow().getDecorView()
                        .setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        );
            }
        } catch (Exception ignored) {}
    }

    // ─────────────────────────────────────────────
    // Icon drawable
    // ─────────────────────────────────────────────

    static class SpeedIconDrawable extends Drawable {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        @Override
        public void draw(Canvas canvas) {
            int w = getBounds().width(), h = getBounds().height();
            float cx = w / 2f, cy = h * 0.7f;
            float r = Math.min(w, h) / 2f * 0.8f;

            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(r * 0.1f);
            canvas.drawArc(new RectF(cx-r, cy-r, cx+r, cy+r), 180, 180, false, paint);

            paint.setStrokeWidth(r * 0.06f);
            for (int i = 0; i < 3; i++) {
                float rad = (float) Math.toRadians(180 + i * 45);
                float cos = (float) Math.cos(rad), sin = (float) Math.sin(rad);
                canvas.drawLine(cx+r*0.8f*cos, cy+r*0.8f*sin, cx+r*cos, cy+r*sin, paint);
            }

            paint.setStrokeWidth(r * 0.08f);
            float nr = (float) Math.toRadians(200);
            canvas.drawLine(cx, cy,
                    cx + r*0.6f*(float)Math.cos(nr),
                    cy + r*0.6f*(float)Math.sin(nr), paint);

            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, r * 0.06f, paint);
        }

        @Override public void setAlpha(int a) { paint.setAlpha(a); }
        @Override public void setColorFilter(ColorFilter cf) { paint.setColorFilter(cf); }
        @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
        @Override public int getIntrinsicWidth()  { return Dim.dp32; }
        @Override public int getIntrinsicHeight() { return Dim.dp32; }
    }
}