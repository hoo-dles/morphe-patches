/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.bend.shared.pairip

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.shared.misc.pairip.getStripPairipPatch

internal val stripPairipPatch = bytecodePatch {
    dependsOn(getStripPairipPatch("bend"))

    execute {
        // After patching pairip, sentry_init() in libsentry.so hangs on pthread_mutex_lock. So let's
        // just block sentry's NDK initialization.
        SentryNdkInitFingerprint.method.returnEarly()

        // JNI method com.adjust.sdk.sig.NativeLibHelper::nSign() also segfaults for some reason. But,
        // again, we don't need it.
        AdjustNSignFingerprint.method.returnEarly(null)
    }
}