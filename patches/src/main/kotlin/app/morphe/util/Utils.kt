package app.morphe.util

import app.morphe.patcher.patch.PatchException

fun Boolean.toHexString(): String = if (this) "0x1" else "0x0"

/**
 * Convert a string representing a pattern of hexadecimal bytes to a byte array.
 *
 * @return The byte array representing the pattern.
 * @throws PatchException If the pattern is invalid.
 */
fun byteArrayOf(pattern: String): ByteArray {
    val stripped = pattern.replace(Regex("\\s"), "")
    check(stripped.length % 2 == 0) { "Must have an even length" }

    try {
    return stripped
        .chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
    } catch (e: NumberFormatException) {
        throw PatchException(
            "Could not parse pattern: $pattern. A pattern is a sequence of case insensitive strings " +
                    "representing hexadecimal bytes separated by spaces",
            e,
        )
    }
}

fun ByteArray.toInt(littleEndian: Boolean = false): Int {
    if (size != 4)
        throw IllegalArgumentException("ByteArray must be exactly 4 bytes long")

    val bytes = if (littleEndian) reversedArray() else this

    return (bytes[0].toInt() and 0xFF) shl 24 or
            (bytes[1].toInt() and 0xFF) shl 16 or
            (bytes[2].toInt() and 0xFF) shl 8 or
            (bytes[3].toInt() and 0xFF)
}