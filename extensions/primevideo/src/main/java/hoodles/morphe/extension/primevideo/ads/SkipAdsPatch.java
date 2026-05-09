package hoodles.morphe.extension.primevideo.ads;

import android.os.Handler;
import android.os.Looper;

import com.amazon.avod.fsm.SimpleTrigger;
import com.amazon.avod.media.ads.AdBreak;
import com.amazon.avod.media.ads.internal.state.AdBreakTrigger;
import com.amazon.avod.media.ads.internal.state.AdEnabledPlayerTriggerType;
import com.amazon.avod.media.ads.internal.state.ServerInsertedAdBreakState;
import com.amazon.avod.media.playback.VideoPlayer;

import app.morphe.extension.shared.Logger;

@SuppressWarnings("unused")
public final class SkipAdsPatch {
    private static final Handler HANDLER =
            new Handler(Looper.getMainLooper());

    public static void enterServerInsertedAdBreakState(ServerInsertedAdBreakState state, AdBreakTrigger trigger, VideoPlayer player) {
        try {
            AdBreak adBreak = trigger.getBreak();

            // There are two scenarios when entering the original method:
            //  1. Player naturally entered an ad break while watching a video.
            //  2. User is skipped/scrubbed to a position on the timeline. If seek position is past an ad break,
            //     user is forced to watch an ad before continuing.
            //
            // Scenario 2 is indicated by trigger.getSeekStartPosition() != null, so skip directly to the scrubbing
            // target. Otherwise, just calculate when the ad break should end and skip to there.
            final long seekTarget;

            if (trigger.getSeekStartPosition() != null)
                seekTarget = trigger.getSeekTarget().getTotalMilliseconds();
            else
                seekTarget = player.getCurrentPosition() + adBreak.getDurationExcludingAux().getTotalMilliseconds();

            Logger.printDebug(() ->
                    "[SkipAds] burst seek target=" + seekTarget);

            // Simulate rapid seek spam similar to repeatedly pressing seek buttons.
            burstSeek(player, seekTarget);

            // Send "end of ads" trigger to state machine so everything doesn't get wacky.
            state.doTrigger(new SimpleTrigger(AdEnabledPlayerTriggerType.NO_MORE_ADS_SKIP_TRANSITION));
        } catch (Exception ex) {
            Logger.printException(() -> "Failed skipping ads", ex);
        }
    }

    private static void burstSeek(VideoPlayer player, long target) {
        long[] offsets = new long[] {
                -1500L,
                -750L,
                0L,
                750L,
                1500L,
                0L
        };

        long delay = 0L;

        for (long offset : offsets) {
            final long seekPos =
                    Math.max(0L, target + offset);

            HANDLER.postDelayed(() -> {
                try {
                    player.seekTo(seekPos);

                    Logger.printDebug(() ->
                            "[SkipAds] seekTo=" + seekPos);

                } catch (Throwable ignored) {
                }
            }, delay);

            delay += 40L;
        }
    }
}
