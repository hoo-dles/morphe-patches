package hoodles.morphe.patches.ling.premium

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.rawResourcePatch
import hoodles.morphe.patches.ling.shared.Constants
import java.security.MessageDigest

/**
 * Ling is a React Native app; its entire Pro entitlement decision lives in the Hermes bytecode
 * bundle (`assets/index.android.bundle`), not in Java/smali. There is no smali method to fingerprint.
 *
 * The whole UI gates Pro through one Redux selector, compiled to Hermes function #28831:
 *
 *     selectIsProUser = (state) => state.payments.isProUser
 *
 * Its bytecode (Hermes bytecode version 96) is a unique 17-byte sequence in the bundle:
 *
 *     6C 00 01              LoadParam    r0, arg1          ; state
 *     37 00 00 01 A9 7B     GetById      r0, r0, 'payments'
 *     37 00 00 02 92 7C     GetById      r0, r0, 'isProUser'
 *     5C 00                 Ret          r0
 *
 * We overwrite the first four bytes with `LoadConstTrue r0 ; Ret r0`, so the selector returns `true`
 * immediately and every consumer sees the user as Pro — regardless of server-validated payment state.
 * The remaining bytes become dead code after the early `Ret`, so the function length is unchanged and
 * no offsets shift.
 *
 * The Hermes file ends with a 20-byte SHA-1 of everything before it; we recompute that footer after
 * the edit so the bundle stays internally consistent.
 */
@Suppress("unused")
val enablePremiumPatch = rawResourcePatch(
    name = "Enable Premium",
    description = "Unlocks Ling Pro by forcing the Hermes selectIsProUser selector to always return true.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val signature = byteArrayOfInts(
            0x6C, 0x00, 0x01,
            0x37, 0x00, 0x00, 0x01, 0xA9, 0x7B,
            0x37, 0x00, 0x00, 0x02, 0x92, 0x7C,
            0x5C, 0x00,
        )
        // LoadConstTrue r0 ; Ret r0
        val replacement = byteArrayOfInts(0x78, 0x00, 0x5C, 0x00)

        val bundle = get("assets/index.android.bundle")
        val data = bundle.readBytes()

        val offset = data.indexOfSingleOrThrow(signature)
        System.arraycopy(replacement, 0, data, offset, replacement.size)

        // Recompute the trailing SHA-1 footer over all preceding bytes.
        val footerStart = data.size - SHA1_LENGTH
        if (footerStart <= 0) throw PatchException("index.android.bundle is too small to be a Hermes bundle.")
        val digest = MessageDigest.getInstance("SHA-1").digest(data.copyOfRange(0, footerStart))
        System.arraycopy(digest, 0, data, footerStart, SHA1_LENGTH)

        bundle.writeBytes(data)
    }
}

private const val SHA1_LENGTH = 20

private fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { ints[it].toByte() }

/**
 * Returns the single offset where [pattern] occurs, throwing if it is absent or ambiguous so the
 * patch fails loudly on a changed build instead of corrupting the bundle.
 */
private fun ByteArray.indexOfSingleOrThrow(pattern: ByteArray): Int {
    val matches = ArrayList<Int>(2)
    outer@ for (i in 0..size - pattern.size) {
        for (j in pattern.indices) {
            if (this[i + j] != pattern[j]) continue@outer
        }
        matches.add(i)
    }
    if (matches.size != 1) {
        throw PatchException(
            "Expected exactly one selectIsProUser signature in index.android.bundle but found " +
                "${matches.size}. The Hermes bundle layout changed — re-run target-hunter for this build.",
        )
    }
    return matches[0]
}
