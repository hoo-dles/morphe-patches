/**
 * Original code is credited to Morphe:
 * https://github.com/MorpheApp/morphe-patches-library/blob/main/patch-library/src/main/kotlin/app/morphe/patches/all/misc/hex/HexPatchBuilder.kt
 *
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.hex

import app.morphe.patcher.patch.PatchException
import app.morphe.util.byteArrayOf
import hoodles.morphe.util.find

/**
 * Represents a pattern to search for and its replacement pattern in a file.
 *
 * @property bytes The bytes to search for.
 * @property replacementBytes The bytes to replace the [bytes] with.
 * @property targetFilePath The path to the file to make the changes in relative to the APK root.
 */
class Replacement(
    private val bytes: ByteArray,
    replacementBytes: ByteArray,
    internal val targetFilePath: String,
) {
    val replacementBytesPadded = replacementBytes + ByteArray(bytes.size - replacementBytes.size)

    @Deprecated("Use the constructor with ByteArray parameters instead.")
    constructor(
        pattern: String,
        replacementPattern: String,
        targetFilePath: String,
    ) : this(
        byteArrayOf(pattern),
        byteArrayOf(replacementPattern),
        targetFilePath
    )

    /**
     * Replaces the [bytes] with the [replacementBytes] in the [targetFileBytes].
     *
     * @param targetFileBytes The bytes of the file to make the changes in.
     */
    internal fun replacePattern(targetFileBytes: ByteArray) {
        val startIndex = targetFileBytes.find(this.bytes)

        if (startIndex == -1) {
            throw PatchException(
                "Pattern not found in target file: " +
                        bytes.joinToString(" ") { "%02x".format(it) }
            )
        }

        replacementBytesPadded.copyInto(targetFileBytes, startIndex)
    }
}