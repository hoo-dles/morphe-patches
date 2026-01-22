package app.morphe.patches.shared.misc.hermes

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.shared.misc.hex.Replacement
import app.morphe.util.byteArrayOf
import app.morphe.util.toInt
import java.io.FileNotFoundException
import java.security.MessageDigest

const val HERMES_BUNDLE_PATH = "assets/index.android.bundle"
val HERMES_MAGIC = byteArrayOf("C6 1F BC 03 C1 03 19 1F")

fun hermesPatch(supplier: () -> Set<Pair<String, String>>) =
    rawResourcePatch() {
        execute {
            val file = get(HERMES_BUNDLE_PATH, true)
            if (!file.exists())
                throw FileNotFoundException("Hermes bytecode bundle not found at: $HERMES_BUNDLE_PATH")

            var fileBytes = file.readBytes()

            // Check Hermes magic number in header
            if (!fileBytes.take(8).toByteArray().contentEquals(HERMES_MAGIC))
                throw Exception("Invalid Hermes file")

            // Get Hermes bytecode version from header
            val version = fileBytes.sliceArray(8..11).toInt(true)

            // Do byte replacements
            supplier()
                .map { Replacement(byteArrayOf(it.first), byteArrayOf(it.second), HERMES_BUNDLE_PATH) }
                .forEach { it.replacePattern(fileBytes) }

            // Recalculate SHA-1 footer hash
            if (version > 74) {
                val hashContent = fileBytes.dropLast(20).toByteArray()
                val md = MessageDigest.getInstance("SHA-1")
                val hash = md.digest(hashContent)
                fileBytes = hashContent + hash
            }

            file.writeBytes(fileBytes)
        }
    }