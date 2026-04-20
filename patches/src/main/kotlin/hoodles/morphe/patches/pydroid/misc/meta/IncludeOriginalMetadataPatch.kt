package hoodles.morphe.patches.pydroid.misc.meta

import app.morphe.patcher.patch.rawResourcePatch
import java.io.File

// Okay, so *somewhere* in the original APK the signatures are being checked, causing any call to
// the included `python3` binary to return an anti-tamper message. I was not able to track down
// how and where this check is being made. However, taking cues from a pre-patched APK, if we copy
// the original META-INF directory (with the certificates) and rename it to "META-iNF", this
// successfully bypasses the signature check... Without knowing how the check is actually
// performed, I can only speculate as to why this works, but it does.
//
// NOTE: Patching will fail silently on a Windows device because of the case-insensitive
//       filesystem.
val includeOriginalMetadataPatch = rawResourcePatch {
    execute {
        val metaInf = get("META-INF", true)
        metaInf.renameTo(File(metaInf.parent, "META-iNF"))
    }
}