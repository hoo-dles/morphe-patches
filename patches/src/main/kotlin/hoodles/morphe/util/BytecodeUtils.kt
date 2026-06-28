package hoodles.morphe.util

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.util.proxy.mutableTypes.MutableClass
import app.morphe.patcher.util.proxy.mutableTypes.MutableField
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.iface.ClassDef

/**
 * Get the first constructor.
 */
internal fun MutableClass.constructor(isStatic: Boolean = false) =
    this.methods.first { it.name == (if (isStatic) "<clinit>" else "<init>") }

/**
 * Returns true if provided `AccessFlags` is set.
 */
internal fun ClassDef.hasFlag(flag: AccessFlags ) = this.accessFlags and flag.value > 0

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

/**
 * Get the first field with the given name.
 */
internal fun MutableClass.fieldByName(name: String): MutableField {
    return this.fields.first { it.name == name }
}

/**
 * Get the public toString() method.
 */
internal fun ClassDef.toStringMethod() =
    this.methods.first { it.name == "toString" && AccessFlags.PUBLIC.isSet(it.accessFlags) }

/**
 * Add instructions `indexFromEnd` places before the end of the method.
 */
internal fun MutableMethod.addInstructionsToEnd(indexFromEnd: Int, smaliInstructions: String) =
    this.addInstructions(this.instructions.count() - indexFromEnd, smaliInstructions)

/**
 * Add instructions to end of method before final return instruction.
 */
internal fun MutableMethod.addInstructionsToEnd(smaliInstructions: String) =
    this.addInstructionsToEnd(1, smaliInstructions)

private const val RETURN_TYPE_MISMATCH = "Mismatch between override type and Method return type"

/**
 * Overrides the first instruction of a method with a boxed `java.lang.Boolean` return value.
 * None of the method code will ever execute.
 */
fun MutableMethod.returnBoxedBooleanEarly(value: Boolean, force: Boolean = false) {
    if (!force)
        check(returnType == "Ljava/lang/Boolean;") { RETURN_TYPE_MISMATCH }
    addInstructions(0,
        """
            sget-object v0, Ljava/lang/Boolean;->${if (value) "TRUE" else "FALSE" }:Ljava/lang/Boolean;
            return-object v0
        """.trimIndent())
}