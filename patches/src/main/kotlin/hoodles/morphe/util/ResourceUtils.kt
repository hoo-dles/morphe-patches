package hoodles.morphe.util

// Matches unescaped double quotes.
private val UNESCAPED_DOUBLE_QUOTE = Regex("(?<!\\\\)\"")

// Matches unescaped single or double quotes.
private val UNESCAPED_QUOTE = Regex("(?<!\\\\)['\"]")

/**
 * @param key String key
 * @param value Text to validate and sanitize
 * @param filePath Path to include in any exception thrown.
 * @param throwException If true, will throw an exception on problems; otherwise, sanitizes.
 * @return sanitized string
 */
internal fun sanitizeAndroidResourceString(
    key: String,
    value: String,
    filePath: String? = null,
    throwException: Boolean = false
): String {
    var sanitized = value

    // Could check for other invalid strings, but for now just check quotes.
    if (value.startsWith('"') && value.endsWith('"')) {
        // Raw strings allow unescaped single quotes but not double quotes.
        val inner = value.substring(1, value.length - 1)
        if (UNESCAPED_DOUBLE_QUOTE.containsMatchIn(inner)) {
            val message = "$filePath String $key contains unescaped double quotes: $value"
            if (throwException) throw IllegalArgumentException(message)
            sanitized = "\"" + UNESCAPED_DOUBLE_QUOTE.replace(inner, "") + "\""
        }
    } else {
        if (value.contains('\n')) {
            val message = "$filePath String $key is not raw but contains newline characters: $value"
            if (throwException) throw IllegalArgumentException(message)
        }

        if (UNESCAPED_QUOTE.containsMatchIn(value)) {
            val message = "$filePath String $key contains unescaped quotes: $value"
            if (throwException) throw IllegalArgumentException(message)
            sanitized = UNESCAPED_QUOTE.replace(value, "")
        }
    }

    return sanitized
}