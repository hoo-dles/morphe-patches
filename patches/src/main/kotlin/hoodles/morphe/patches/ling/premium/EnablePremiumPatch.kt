package hoodles.morphe.patches.ling.premium

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.rawResourcePatch
import hoodles.morphe.patches.ling.shared.Constants
import java.security.MessageDigest

@Suppress("unused")
val enablePremiumPatch = rawResourcePatch(
    name = "Enable Premium",
    description = "Unlocks Ling Pro.",
) {
    compatibleWith(Constants.COMPATIBILITY)

    execute {
        val signature = byteArrayOfInts(
            0x6C, 0x00, 0x01,
            0x37, 0x00, 0x00, 0x01, 0xA9, 0x7B,
            0x37, 0x00, 0x00, 0x02, 0x92, 0x7C,
            0x5C, 0x00,
        )
        val replacement = byteArrayOfInts(0x78, 0x00, 0x5C, 0x00)

        val bundle = get("assets/index.android.bundle")
        val data = bundle.readBytes()

        val offset = data.indexOfSingleOrThrow(signature)
        System.arraycopy(replacement, 0, data, offset, replacement.size)

        val footerStart = data.size - SHA1_LENGTH
        if (footerStart <= 0) throw PatchException("index.android.bundle is too small to be a Hermes bundle.")
        val digest = MessageDigest.getInstance("SHA-1").digest(data.copyOfRange(0, footerStart))
        System.arraycopy(digest, 0, data, footerStart, SHA1_LENGTH)

        bundle.writeBytes(data)
    }
}

private const val SHA1_LENGTH = 20

private fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { ints[it].toByte() }

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
