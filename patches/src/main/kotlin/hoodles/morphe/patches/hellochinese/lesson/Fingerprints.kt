/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.hellochinese.lesson

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

private const val HC3_BASE_LESSON_ACTIVITY_CLASS =
    "Lcom/hellochinese/lesson/activitys/HC3BaseLessonActivity;"

/**
 * Matches HC3BaseLessonActivity.V0() method.
 *
 * The method's bytecode pattern:
 *   invoke-super {p0}, BasicLessonActivity;->V0()V
 *   iget-boolean v0, p0, HC3BaseLessonActivity;->r2:Z
 *   if-eqz v0, :cond_0
 *   iget-object v0, p0, BasicLessonActivity;->P:Lx1/c;
 *   invoke-interface {v0}, Lx1/c;->getCurrentQuestionWrongTime()I
 *   move-result v0
 *   const/4 v1, 0x3          <-- this is the threshold we want to change
 *   if-lt v0, v1, :cond_0
 */
internal object SkipOnFirstMistakeFingerprint : Fingerprint(
    definingClass = HC3_BASE_LESSON_ACTIVITY_CLASS,
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf(),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.INVOKE_SUPER,
        Opcode.IGET_BOOLEAN,
        Opcode.IF_EQZ,
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_INTERFACE,
        Opcode.MOVE_RESULT,
        Opcode.CONST_4,
        Opcode.IF_LT,
    )
)

/**
 * Matches CheckPanel.h() method — and ONLY h(), not resetView().
 *
 * h() is exactly 7 instructions ending in RETURN_VOID:
 *   iget-object v0, p0, CheckPanel;->mContinueBtnLayout
 *   const/16 v1, 0x8
 *   invoke-virtual {v0, v1}, View;->setVisibility(I)V
 *   iget-object v0, p0, CheckPanel;->skpiLayout
 *   const/4 v1, 0x0
 *   invoke-virtual {v0, v1}, View;->setVisibility(I)V
 *   return-void
 *
 * resetView() starts with the same 6 opcodes but continues with more
 * instructions before returning, so including RETURN_VOID disambiguates.
 */
internal object CheckPanelHMethodFingerprint : Fingerprint(
    definingClass = "Lcom/hellochinese/views/widgets/CheckPanel;",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf(),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.IGET_OBJECT,
        Opcode.CONST_16,
        Opcode.INVOKE_VIRTUAL,
        Opcode.IGET_OBJECT,
        Opcode.CONST_4,
        Opcode.INVOKE_VIRTUAL,
        Opcode.RETURN_VOID,
    )
)
