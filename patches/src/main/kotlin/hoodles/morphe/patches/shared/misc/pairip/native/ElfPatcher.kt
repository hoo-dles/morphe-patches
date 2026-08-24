package hoodles.morphe.patches.shared.misc.pairip.native

object ElfPatcher {
     init {
         CrossEnvNativeLoader.load("elf_jni_patcher")
     }

     @JvmStatic
     external fun addRelocations(path: String, patches: Array<RelocationEntry>): Boolean
}