/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.alltrails.tracking

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.util.returnEarly
import hoodles.morphe.patches.alltrails.shared.Constants
import java.util.logging.Logger

private fun MutableMethod.returnThis() {
    addInstructions(0, "return-object p0")
}

@Suppress("unused")
val disableTrackingPatch = bytecodePatch(
    name = "Disable tracking and analytics",
    description = "Disables non-essential third-party tracking and analytics SDKs " +
        "(Amplitude, AppsFlyer, Braze, Firebase Analytics, Mapbox telemetry) while leaving " +
        "core mapping, GPS recording, login, and sync intact. Firebase Crashlytics is left " +
        "enabled. Braze marketing push / in-app messages may stop working.",
    default = false
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val logger = Logger.getLogger("DisableTrackingPatch")
        val blocked = mutableListOf<String>()

        fun tryBlock(sdk: String, fingerprintMethod: MutableMethod?, block: (MutableMethod) -> Unit) {
            if (fingerprintMethod == null) {
                logger.warning("Could not find $sdk hook; skipping")
                return
            }
            if (fingerprintMethod.implementation == null) {
                logger.warning("Skipping $sdk hook: method has no implementation (abstract/native)")
                return
            }
            try {
                block(fingerprintMethod)
                blocked.add(sdk)
            } catch (error: Exception) {
                logger.warning("Failed to apply $sdk hook: ${error.message}")
            }
        }

        tryBlock("AppsFlyer.isStopped", AppsFlyerIsStoppedFingerprint.methodOrNull) {
            it.returnEarly(true)
        }
        tryBlock("AppsFlyer.init", AppsFlyerInitFingerprint.methodOrNull) {
            it.returnThis()
        }

        tryBlock("Amplitude.logEvent", AmplitudeLogEventFingerprint.methodOrNull) {
            it.returnEarly()
        }
        tryBlock("AmplitudeClient.logEvent", AmplitudeClientLogEventFingerprint.methodOrNull) {
            it.returnEarly()
        }
        tryBlock("AmplitudeClient.initialize", AmplitudeClientInitializeFingerprint.methodOrNull) {
            it.returnThis()
        }

        tryBlock("FirebaseAnalytics.logEvent", FirebaseAnalyticsLogEventFingerprint.methodOrNull) {
            it.returnEarly()
        }
        tryBlock(
            "FirebaseAnalytics.setAnalyticsCollectionEnabled",
            FirebaseAnalyticsSetCollectionEnabledFingerprint.methodOrNull
        ) {
            it.returnEarly()
        }

        tryBlock("Braze.logCustomEvent", BrazeLogCustomEventFingerprint.methodOrNull) {
            it.returnEarly()
        }
        tryBlock("Braze.openSession", BrazeOpenSessionFingerprint.methodOrNull) {
            it.returnEarly()
        }

        tryBlock("Mapbox TelemetryUtils.initialize", MapboxTelemetryInitializeFingerprint.methodOrNull) {
            it.returnEarly()
        }
        tryBlock("Mapbox onAppUserTurnstileEvent", MapboxTurnstileFingerprint.methodOrNull) {
            it.returnEarly()
        }
        tryBlock("Mapbox sendMapLoadEvent", MapboxMapLoadEventFingerprint.methodOrNull) {
            it.returnEarly()
        }

        if (blocked.isEmpty()) {
            logger.warning("No tracking SDK hooks applied")
        } else {
            logger.info("Disabled tracking hooks: ${blocked.joinToString(", ")}")
        }
    }
}
