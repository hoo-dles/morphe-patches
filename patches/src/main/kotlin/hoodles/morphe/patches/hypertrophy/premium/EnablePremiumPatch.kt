/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.hypertrophy.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.patches.all.misc.extension.activityOnCreateExtensionHook
import app.morphe.patches.all.misc.extension.sharedExtensionPatch
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35c
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodParameter
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference
import hoodles.morphe.patches.all.pairip.license.disableLicenseCheckPatch

val extensionPatch = sharedExtensionPatch(
    "hypertrophy",
    activityOnCreateExtensionHook()
)

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        packageName = "com.rp.hypertrophy",
        name = "RP Hypertrophy",
        appIconColor = 0xec0915,
        targets = listOf(AppTarget("1.1.0"))
    ))

    dependsOn(extensionPatch, disableLicenseCheckPatch)

    execute {
        val webClientClass = WebViewClientClassFingerprint.classDef

        val impl = MutableMethodImplementation(4)
        impl.addInstructions(
            listOf(
                BuilderInstruction35c(
                    Opcode.INVOKE_STATIC,
                    1,
                    3,
                    0, 0, 0, 0,
                    ImmutableMethodReference(
                        "Lhoodles/morphe/extension/hypertrophy/premium/EnablePremiumPatch;",
                        "patchSubscriptionRequest",
                        listOf("Landroid/webkit/WebResourceRequest;"),
                        "Landroid/webkit/WebResourceResponse;"
                    )
                ),
                BuilderInstruction11x(Opcode.MOVE_RESULT_OBJECT, 0),
                BuilderInstruction11x(Opcode.RETURN_OBJECT, 0)
            )
        )

        val method = ImmutableMethod(
            webClientClass.type,
            "shouldInterceptRequest",
            listOf(
                ImmutableMethodParameter("Landroid/webkit/WebView;", null, "view"),
                ImmutableMethodParameter("Landroid/webkit/WebResourceRequest;", null, "request")
            ),
            "Landroid/webkit/WebResourceResponse;",
            AccessFlags.PUBLIC.value,
            null,
            null,
            impl
        )

        webClientClass.methods.add(MutableMethod(method))
    }
}