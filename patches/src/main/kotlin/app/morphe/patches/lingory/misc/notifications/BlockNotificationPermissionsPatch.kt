package app.morphe.patches.lingory.misc.notifications

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.shared.misc.hex.hexPatch

@Suppress("unused")
val blockNotificationPermissionsPatch = rawResourcePatch(
    name = "Block Permissions Request",
    description = "Blocks the request of notification permissions on load of app."
) {
    compatibleWith("org.languageapp.lingory"("1.2.75"))

    dependsOn(
        hexPatch(block = {
            // mov     x1, x0
            // bl      #0x9e2960 (NotificationController::requestPermission)  --> nop
            "e1 03 00 aa 6f 01 00 94" asPatternTo "e1 03 00 aa 1f 20 03 d5" inFile "lib/arm64-v8a/libapp.so"
        })
    )
}