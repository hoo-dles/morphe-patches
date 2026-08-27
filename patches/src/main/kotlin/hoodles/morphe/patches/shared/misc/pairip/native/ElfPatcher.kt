/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.pairip.native

import app.morphe.patcher.patch.ResourcePatchContext

object ElfPatcher {
    @Volatile
    private var initialized = false

    fun init(context: ResourcePatchContext) {
        synchronized(this) {
            if (!initialized) {
                CrossEnvNativeLoader.load("elf_jni_patcher", context)
                initialized = true
            }
        }
     }

    @JvmStatic
    fun patch(path: String, patches: Array<RelocationEntry>): Boolean {
        check(initialized) { "ElfPatcher must be initialized via init(...) before use." }
        return addRelocations(path, patches)
    }

    @JvmStatic
    private external fun addRelocations(path: String, patches: Array<RelocationEntry>): Boolean
}