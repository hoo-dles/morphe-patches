/**
 * Original code credited to Morphe:
 * https://github.com/MorpheApp/morphe-patches/blob/main/patches/src/main/kotlin/app/morphe/patches/util/resource/StringResource.kt
 */

package hoodles.morphe.util

import app.morphe.patcher.apk.ApkSignatureScheme
import java.security.MessageDigest
import java.security.cert.X509Certificate

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

private fun sortOrder(scheme: ApkSignatureScheme) = when (scheme) {
    ApkSignatureScheme.V31 -> 0
    ApkSignatureScheme.V3 -> 1
    ApkSignatureScheme.V2 -> 2
    else -> 99
}

class NoCertificateException : Exception("Unable to extract certificate from apk")

fun getEndEntityCertificate(
    schemeToCertsMap: Map<ApkSignatureScheme, List<X509Certificate>>
): X509Certificate {
    // scheme map can have empty lists for some reason
    val filteredMap = schemeToCertsMap.filterValues { it.isNotEmpty() }

    val highestSchemeVersion = filteredMap.keys.minByOrNull { sortOrder(it) } ?: throw NoCertificateException()
    val certsForScheme = filteredMap[highestSchemeVersion] ?: throw NoCertificateException()

    if (certsForScheme.isEmpty()) throw NoCertificateException()

    // if single/self-signed, it is the developer cert
    if (certsForScheme.size == 1) {
        return certsForScheme.first()
    }

    // if a cert chain exists, find the leaf
    val issuerPrincipals = certsForScheme.map { it.issuerX500Principal }.toSet()
    return certsForScheme.firstOrNull { cert ->
        !issuerPrincipals.contains(cert.subjectX500Principal)
    } ?: certsForScheme.first()
}

fun isCertMaybeInauthentic(cert: X509Certificate): Boolean {
    if (cert.subjectX500Principal.name.contains("morphe", true))
        return true

    val digest = MessageDigest.getInstance("SHA-1").digest(cert.encoded)
    return isCertMaybeInauthentic(digest.joinToString("") { "%02x".format(it) })
}

val KNOWN_SHA1 = listOf(
    "e94e3afa40a54ecee4eef83f580393507fcd205a", // AntiSplit M
    "61ed377e85d386a8dfee6b864bd85b0bfaa5af81", // public debug certificate
)
private fun isCertMaybeInauthentic(certSHA1: String) = KNOWN_SHA1.contains(certSHA1.lowercase())