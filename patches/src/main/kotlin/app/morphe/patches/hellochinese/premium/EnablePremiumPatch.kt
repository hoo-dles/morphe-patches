package app.morphe.patches.hellochinese.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.misc.hex.ARM64_NOP
import app.morphe.patches.shared.misc.hex.hexPatch
import app.morphe.util.returnEarly

private val nativePatch = hexPatch( block = {
    val lib = "lib/arm64-v8a/libalg.so"

    // patch isValidApp default value
    "00 00 80 12 A0 37 00 B9" asPatternTo "00 00 80 52 A0 37 00 B9" inFile lib

    // force SignData.meta[1] = 0
    "01 C0 00 51 A0 53 40 F9 01 04 00 B9" asPatternTo "01 00 80 52 A0 53 40 F9 01 04 00 B9" inFile lib
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