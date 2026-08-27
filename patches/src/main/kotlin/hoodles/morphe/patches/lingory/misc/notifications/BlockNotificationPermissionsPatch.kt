/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.lingory.misc.notifications

import app.morphe.patcher.patch.rawResourcePatch
import hoodles.morphe.patches.lingory.shared.Constants
import app.morphe.patches.all.misc.hex.hexPatch
import hoodles.morphe.util.requireArm64

@Suppress("unused")
val blockNotificationPermissionsPatch = rawResourcePatch(
    name = "Block Permissions Request",
    description = "Blocks the request of notification permissions on load of app. Requirements: arm64-v8a, strict apk version"
) {
    compatibleWith(Constants.COMPATIBILITY)

    availability(requireArm64())

    dependsOn(
        hexPatch(block = {
            // mov     x1, x0
            // bl      #0x9e2960 (NotificationController::requestPermission)  --> nop
            "e1 03 00 aa 6f 01 00 94" asPatternTo "e1 03 00 aa 1f 20 03 d5" inFile "lib/arm64-v8a/libapp.so"
        })
    )
}