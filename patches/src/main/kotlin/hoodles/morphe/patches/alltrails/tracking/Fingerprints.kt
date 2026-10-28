/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.alltrails.tracking

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

private const val APPS_FLYER_LIB = "Lcom/appsflyer/AppsFlyerLib;"

internal object AppsFlyerIsStoppedFingerprint : Fingerprint(
    name = "isStopped",
    returnType = "Z",
    parameters = emptyList(),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    custom = { _, classDef -> classDef.superclass == APPS_FLYER_LIB }
)

internal object AppsFlyerInitFingerprint : Fingerprint(
    name = "init",
    returnType = APPS_FLYER_LIB,
    parameters = listOf(
        "Ljava/lang/String;",
        "Lcom/appsflyer/AppsFlyerConversionListener;",
        "Landroid/content/Context;"
    ),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    custom = { _, classDef -> classDef.superclass == APPS_FLYER_LIB }
)

internal object AmplitudeLogEventFingerprint : Fingerprint(
    definingClass = "Lcom/amplitude/api/Amplitude;",
    name = "logEvent",
    returnType = "V",
    parameters = listOf("Ljava/lang/String;")
)

internal object AmplitudeClientLogEventFingerprint : Fingerprint(
    definingClass = "Lcom/amplitude/api/AmplitudeClient;",
    name = "logEvent",
    returnType = "V",
    parameters = listOf("Ljava/lang/String;")
)

internal object AmplitudeClientInitializeFingerprint : Fingerprint(
    definingClass = "Lcom/amplitude/api/AmplitudeClient;",
    name = "initialize",
    returnType = "Lcom/amplitude/api/AmplitudeClient;",
    parameters = listOf("Landroid/content/Context;", "Ljava/lang/String;")
)

internal object FirebaseAnalyticsLogEventFingerprint : Fingerprint(
    definingClass = "Lcom/google/firebase/analytics/FirebaseAnalytics;",
    name = "logEvent",
    returnType = "V",
    parameters = listOf("Ljava/lang/String;", "Landroid/os/Bundle;")
)

internal object FirebaseAnalyticsSetCollectionEnabledFingerprint : Fingerprint(
    definingClass = "Lcom/google/firebase/analytics/FirebaseAnalytics;",
    name = "setAnalyticsCollectionEnabled",
    returnType = "V",
    parameters = listOf("Z")
)

internal object BrazeLogCustomEventFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "logCustomEvent",
    returnType = "V",
    parameters = listOf("Ljava/lang/String;", "Lcom/braze/models/outgoing/BrazeProperties;")
)

internal object BrazeOpenSessionFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "openSession",
    returnType = "V",
    parameters = listOf("Landroid/app/Activity;")
)

internal object MapboxTelemetryInitializeFingerprint : Fingerprint(
    definingClass = "Lcom/mapbox/common/TelemetryUtils;",
    name = "initialize",
    returnType = "V",
    parameters = emptyList()
)

internal object MapboxTurnstileFingerprint : Fingerprint(
    definingClass = "Lcom/mapbox/maps/module/telemetry/MapTelemetryImpl;",
    name = "onAppUserTurnstileEvent",
    returnType = "V",
    parameters = emptyList()
)

internal object MapboxMapLoadEventFingerprint : Fingerprint(
    definingClass = "Lcom/mapbox/maps/module/telemetry/MapTelemetryImpl;",
    name = "sendMapLoadEvent",
    returnType = "V",
    parameters = emptyList()
)
