/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.all.microg

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import com.android.tools.smali.dexlib2.analysis.reflection.util.ReflectionUtils.javaToDexName
import hoodles.morphe.util.getEndEntityCertificate
import hoodles.morphe.util.isCertMaybeInauthentic
import java.security.MessageDigest
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

internal object MicroGMetadata {
    lateinit var packageName: String
    lateinit var signatureSHA1: String
    lateinit var onCreateFingerprint: Fingerprint
}

private lateinit var mainActivity: String

private val resourceMetadataPatch = resourcePatch {
    execute {
        MicroGMetadata.packageName = packageMetadata.packageName

        val cert = getEndEntityCertificate(packageMetadata.signingCertificates)

        if (isCertMaybeInauthentic(cert)) throw PatchException("Invalid signing certificate. Original APK is required.")

        val digest = MessageDigest.getInstance("SHA-1").digest(cert.encoded)
        MicroGMetadata.signatureSHA1 = digest.joinToString("") { "%02x".format(it) }

        document("AndroidManifest.xml").use { document ->
            val xPath = XPathFactory.newInstance().newXPath()

            val expression = """
                (//activity | //activity-alias)[
                    intent-filter/action/@*[local-name()='name'] = 'android.intent.action.MAIN'
                ][1]/@*[
                    (local-name() = 'targetActivity' and parent::activity-alias) or
                    (local-name() = 'name' and parent::activity)
                ]
            """.trimIndent()

            val activityName = xPath.evaluate(expression, document, XPathConstants.STRING) as String
            if (activityName.isBlank())
                throw PatchException("Could not find main activity in manifest")

            mainActivity = if (activityName.startsWith(".")) "${MicroGMetadata.packageName}$activityName" else activityName
        }
    }
}

internal val microGMetadataPatch = bytecodePatch {
    dependsOn(resourceMetadataPatch)

    execute {
        fun firstOnCreateClass(className: String): String {
            val clazz = classDefBy(className)
            val hasOnCreate = clazz.methods.any() { it.name == "onCreate" }
            if (hasOnCreate) return className

            return clazz.superclass?.let {  firstOnCreateClass(it) } ?:
            throw PatchException("Could not find onCreate method for MicroG hook")
        }

        val onCreateClass = firstOnCreateClass(javaToDexName(mainActivity))
        MicroGMetadata.onCreateFingerprint = Fingerprint(
            definingClass = onCreateClass,
            name = "onCreate",
            returnType = "V",
        )
    }
}