/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.util

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.util.proxy.mutableTypes.MutableClass
import app.morphe.patcher.util.proxy.mutableTypes.MutableField
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * Get the first constructor.
 */
internal val MutableClass.constructor: MutableMethod
    get() = this.methods.first { it.name == "<init>" }

/**
 * Get the first static constructor.
 */
internal val MutableClass.staticConstructor: MutableMethod
    get() = this.methods.first { it.name == "<clinit>" }

/**
 * Remove the given AccessFlags from the field.
 */
internal fun MutableField.removeFlag(vararg flags: AccessFlags) {
    this.accessFlags = removeFlags(this.accessFlags, *flags)
}

/**
 * Remove the given AccessFlags from the method.
 */
internal fun MutableMethod.removeFlag(vararg flags: AccessFlags) {
    this.accessFlags = removeFlags(this.accessFlags, *flags)
}

/**
 * Remove the given AccessFlags from the class.
 */
internal fun MutableClass.removeFlag(vararg flags: AccessFlags) {
    this.accessFlags = removeFlags(this.accessFlags, *flags)
}

private fun removeFlags(currentFlags: Int, vararg flagsToRemove: AccessFlags): Int {
    val bitField = flagsToRemove.map { it.value }.reduce { acc, flag -> acc and flag }
    return currentFlags and bitField.inv()
}

private const val RETURN_TYPE_MISMATCH = "Mismatch between override type and Method return type"

fun MutableMethod.returnBoxedIntegerEarly(value: Int) {
    check(returnType == "Ljava/lang/Integer;" || returnType == "Ljava/lang/Object;") {
        RETURN_TYPE_MISMATCH
    }

    addInstructions(0,
        """
            const v0, $value
            invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
            move-result-object v0
            return-object v0
        """.trimIndent())
}
