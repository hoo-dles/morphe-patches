package hoodles.morphe.patches.shared.misc.pairip.native

import java.io.EOFException
import java.io.IOException

object IpsPatcher {

    class MalformedIpsException(reason: String) : EOFException("Malformed IPS patch: $reason")

    @Throws(IOException::class)
    fun apply(base: ByteArray, patch: ByteArray): ByteArray {
        if (patch.size < 8 || String(patch, 0, 5, Charsets.US_ASCII) != "PATCH")
            throw MalformedIpsException("Bad header")

        val result = base.copyOf()
        var pos = 5

        while (true) {
            if (pos + 3 > patch.size) throw MalformedIpsException("Missing EOF marker")

            // 3-byte offset
            val offset = ((patch[pos].toInt() and 0xFF) shl 16) or
                    ((patch[pos + 1].toInt() and 0xFF) shl 8) or
                    (patch[pos + 2].toInt() and 0xFF)
            pos += 3

            // "EOF" terminator
            if (offset == 0x454F46) {
                break
            }

            if (pos + 2 > patch.size) throw MalformedIpsException("Truncated record")
            // 2-byte length
            val length = ((patch[pos].toInt() and 0xFF) shl 8) or
                    (patch[pos + 1].toInt() and 0xFF)
            pos += 2

            // RLE if length == 0
            if (length == 0) {
                if (pos + 3 > patch.size) throw MalformedIpsException("Truncated RLE record")
                val rleLen = ((patch[pos].toInt() and 0xFF) shl 8) or
                        (patch[pos + 1].toInt() and 0xFF)
                pos += 2
                val value = patch[pos]
                pos += 1

                for (i in 0 until rleLen) {
                    result[offset + i] = value
                }
            } else {
                if (pos + length > patch.size) throw MalformedIpsException("Truncated literal record")
                patch.copyInto(result, destinationOffset = offset, startIndex = pos, endIndex = pos + length)
                pos += length
            }
        }

        return result
    }
}