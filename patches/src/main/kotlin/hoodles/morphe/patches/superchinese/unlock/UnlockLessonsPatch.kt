/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.superchinese.unlock

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.all.signature.spoofSignaturePatch
import hoodles.morphe.patches.superchinese.shared.Constants
import hoodles.morphe.util.returnBoxedIntegerEarly

enum class SmaliType {
    INT, INTEGER, STRING
}

@Suppress("unused")
val unlockLessonsPatch = bytecodePatch(
    name = "Unlock all lessons",
    description = "Only unlocks lessons on the client UI! This is useful for pre-downloading content during free trial periods."
) {
    compatibleWith(Constants.Compatibility)

    dependsOn(spoofSignaturePatch)

    execute {
        // Unlock all activities in UI
        val unlockClasses = mapOf(
            "Lcom/superchinese/model/BaseLesson;" to SmaliType.INTEGER,
            "Lcom/superchinese/model/LessonStart;" to SmaliType.INTEGER,
            "Lcom/superchinese/model/LessonWordGrammarEntity;" to SmaliType.STRING,
            "Lcom/superchinese/model/LessonCollection;" to SmaliType.INTEGER,
            "Lcom/superchinese/model/LessonViewUnit;" to SmaliType.INTEGER,
            "Lcom/superchinese/model/LevelIndexItem;" to SmaliType.INTEGER,
            "Lcom/superchinese/model/HomeLevelTest;" to SmaliType.INT
        )

        unlockClasses.forEach { (className, type) ->
            getGetUnlockFingerprint(className).method.apply {
                when (type) {
                    SmaliType.INT -> returnEarly(1)
                    SmaliType.INTEGER -> returnBoxedIntegerEarly(1)
                    SmaliType.STRING -> returnEarly("1")
                }
            }
        }

        // Just for UI gates after downloading
        GetVipFingerprint.method.returnEarly(1)

        // Redirect empty lesson start request callback, end(), to error() so that it uses
        // downloaded/cached data.
        LessonStartCallbackCtorFingerprint.classDef.apply {
            val errorMatch = ErrorFingerprint.match(this)
            val errorMethod = errorMatch.method
            // remove null check since we have to pass null as message (insufficient registers)
            errorMethod.removeInstruction(errorMatch.instructionMatches.first().index)

            methods.first { it.name == "end" }.also {
                it.addInstructions(0, """
                    const/4 v0, 0x0
                    invoke-virtual {p0, v0, v0}, $errorMethod
                    return-void
                """.trimIndent())
            }
        }

    }
}