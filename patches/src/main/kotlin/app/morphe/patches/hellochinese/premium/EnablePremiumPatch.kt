package app.morphe.patches.hellochinese.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.misc.hex.ARM64_NOP
import app.morphe.patches.shared.misc.hex.hexPatch
import app.morphe.util.returnEarly

private val nativePatch = hexPatch( block = {
    val lib = "lib/arm64-v8a/libalg.so"

    // patch isValidApp default value
    "00 00 80 12 a0 37 00 b9" asPatternTo "00 00 80 52 a0 37 00 b9" inFile lib

    // prevent jmp when retval of process_sign_str is not 0 or 1
    "01 0f 00 54" asPatternTo ARM64_NOP inFile lib
})

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith("com.hellochinese"("7.9.25"))

    dependsOn(nativePatch)

    execute {
        IsAuthInvalidFingerprint.method.returnEarly(false)
        IsUserTypeOver0Fingerprint.match(IsAuthInvalidFingerprint.classDef).method.returnEarly(true)
        IsUserTypeEqual2Fingerprint.match(IsAuthInvalidFingerprint.classDef).method.returnEarly(true)
    }
}