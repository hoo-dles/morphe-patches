package hoodles.morphe.patches.shared.misc.extension

import app.morphe.patches.all.misc.extension.ExtensionHook
import app.morphe.patches.all.misc.extension.sharedExtensionPatch
import hoodles.morphe.patches.shared.misc.gms.getMainOnCreateFingerprint


/**
 * A patch to extend with an extension shared with multiple patches.
 *
 * @param extensionName The name of the extension to extend with.
 */
fun sharedExtensionPatch(
    extensionName: String,
    vararg hooks: ExtensionHook,
) = sharedExtensionPatch(listOf(extensionName), *hooks)


/**
 * Creates an extension hook from a non-obfuscated activity, which typically is the main activity
 * defined in the app manifest.xml file.
 *
 * @param activityClassType Either the full activity class type such as `Lcom/company/MainActivity;`
 *                          or the 'ends with' string for the activity such as `/MainActivity;`
 * @param targetBundleMethod If the extension should hook `onCreate(Landroid/os/Bundle;)` or `onCreate()`
 */
fun activityOnCreateExtensionHook(activityClassType: String = "/MainActivity;", targetBundleMethod: Boolean = true) =
    ExtensionHook(getMainOnCreateFingerprint(activityClassType, targetBundleMethod))