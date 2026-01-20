package app.morphe.patches.example

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.*
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

// Declaring fingerprints as classes is not required, but if a fingerprint fails
// to match then the exception stack trace will include the fingerprint name.
object AdLoaderFingerprint : Fingerprint(
    // Exact access flags
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    // Return type is matched using String.startsWith()
    returnType = "Z",
    // Declared parameters are matched using String.startsWith()
    // Non obfuscated classes should be declared using the full class name.
    // While obfuscated class names must be declared only using the object type
    // Since obfuscated names change between releases.
    // Last parameter is simply `L` since it's an obfuscated class object.
    parameters = listOf("Ljava/lang/String;", "I", "L"),

    // Instruction filters.
    filters = listOf(
        // Filter 1.
        fieldAccess(
            // Restrict to field get operation.
            opcode = Opcode.IGET,
            // "this" refers to the class the method was declared in.
            // It does not include superclasses or subclasses.
            definingClass = "this",
            type = "Ljava/util/Map;"
        ),

        // Filter 2.
        string("showBannerAds"),

        // Filter 3.
        methodCall(
            definingClass = "Ljava/lang/String;",
            name = "equals",
        ),

        // Filter 4.
        // MatchAfterImmediately() means this must match immediately after the last filter.
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately()),

        // Filter 5.
        literal(1337),

        // Filter 6.
        opcode(Opcode.IF_EQ),
    ),

    custom = { _, classDef ->
        classDef.type == "Lcom/some/app/ads/AdsLoader;"
    }
)