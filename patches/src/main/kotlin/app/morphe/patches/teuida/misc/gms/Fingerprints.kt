package app.morphe.patches.teuida.misc.gms

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object MainActivityOnCreateFingerprint : Fingerprint (
    returnType = "V",
    parameters = listOf("Landroid/os/Bundle;"),
    custom = { method, classDef ->
        method.name == "onCreate" && classDef.endsWith("/MainActivity;")
    }
)

object IsGooglePlayServicesAvailableFingerprint : Fingerprint (
    returnType = "I",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    parameters = listOf("Landroid/content/Context;","I"),
    custom = { method, classDef ->
        method.name == "isGooglePlayServicesAvailable" && classDef.endsWith("/GooglePlayServicesUtilLight;")
    }
)