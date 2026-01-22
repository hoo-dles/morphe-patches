package app.morphe.patches.duolingo.music

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.duolingo.shared.Utils.fieldFromToString
import app.morphe.util.constructor

@Suppress("unused")
val unlockFullSongsPatch = bytecodePatch(
    name = "Unlock licensed songs",
    description = "Allows playing the full version of licensed music instead of the 30-second preview.",
    use = false
) {
    compatibleWith("com.duolingo")

    execute {
        MusicCourseFingerprint.classDef.apply {
            val accessField = this.fieldFromToString("licensedMusicAccess")
            val constructor = this.constructor()
            constructor.addInstructions(
                constructor.instructions.count() - 1,
                """
                    sget-object v0, Lcom/duolingo/data/home/music/LicensedMusicAccess;->FULL:Lcom/duolingo/data/home/music/LicensedMusicAccess;
                    iput-object v0, p0, ${this.type}->${accessField.name}:${accessField.type}
                """.trimIndent()
            )
        }
    }
}