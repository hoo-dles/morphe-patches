package hoodles.morphe.patches.shared.misc.pairip.native

import android.annotation.SuppressLint
import android.content.Context
import app.morphe.util.inputStreamFromBundledResource
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Method
import org.scijava.nativelib.NativeLoader

const val NATIVE_DIR_PREFIX = "pairip/native"

object CrossEnvNativeLoader {
    fun load(libName: String) {
        val isAndroid = System.getProperty("java.vendor")?.contains("Android", ignoreCase = true) == true
        if (isAndroid) AndroidLoader.load(libName) else DesktopLoader.load(libName)
    }
}

private object DesktopLoader {
    fun load(libName: String) {
        NativeLoader.loadLibrary(libName, NATIVE_DIR_PREFIX)
    }
}

private object AndroidLoader {
    @SuppressLint("SetWorldReadable", "UnsafeDynamicallyLoadedCode")
    fun load(libName: String) {
        val targetDir = File(getCodeCache(), "native_libs").apply { mkdirs() }
        val targetSoFile = File(targetDir, "lib$libName.so")

        targetDir.setWritable(true, true)
        targetSoFile.setWritable(true, true)

        inputStreamFromBundledResource("$NATIVE_DIR_PREFIX/android_arm64", "lib$libName.so").use { input ->
            if (input == null)
                throw RuntimeException("Could not extract bundled library ($libName)")

            FileOutputStream(targetSoFile).use { output ->
                input.copyTo(output)
            }
        }

        // Android 15+: Remove write permission before loading for W^X
        targetSoFile.setWritable(false, false)
        targetSoFile.setReadable(true, false)
        targetSoFile.setExecutable(true, false)

        targetDir.setWritable(false, false)
        targetDir.setExecutable(true, false)

        System.load(targetSoFile.absolutePath)
    }

    @SuppressLint("SetWorldReadable", "SetWorldWritable")
    private fun getCodeCache(): File {
        val pkgName = File("/proc/self/cmdline")
            .readText()
            .trim { it <= ' ' }
            .split(":")[0]

        val codeCache = File("/data/data/$pkgName/code_cache")
        val cache = File("/data/data/$pkgName/cache")

        val target = when {
            codeCache.exists() || codeCache.mkdirs() -> codeCache
            cache.exists() || cache.mkdirs() -> cache
            else -> File("/data/user/0/$pkgName/code_cache")
        }

        return target
    }
}
