package hoodles.morphe.patches.adguard.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(Compatibility(
        name = "AdGuard",
        packageName = "com.adguard.android",
        appIconColor = 0x67b279,
        targets = listOf(AppTarget("4.12.81"))
    ))

    execute {
        val licenseTypeClass = PaidLicenseFingerprint.method.parameters[1].type
        val lifetimeDurationInstance = LifetimeDurationFingerprint.classDef.staticFields.first()

        val getPaidLicenseMethod = ImmutableMethod(
            GetPlusStateFingerprint.classDef.type,
            "getPaidLicense",
            null,
            GetPlusStateFingerprint.method.returnType,
            AccessFlags.STATIC.value,
            null,
            null,
            MutableMethodImplementation(7)
        ).toMutable().apply {
            addInstructions(0, """
            new-instance v0, ${PaidLicenseFingerprint.classDef.type}
            const-string v1, ""
            sget-object v2, $licenseTypeClass->Personal:$licenseTypeClass
            sget-object v3, $lifetimeDurationInstance
            const/4 v4, 0x1
            const/4 v5, 0x3
            const-string v6, ""
            invoke-direct/range {v0 .. v6}, ${PaidLicenseFingerprint.method}
            return-object v0
        """.trimIndent())
        }

        GetPlusStateFingerprint.classDef.methods.add(getPaidLicenseMethod)

        GetPlusStateFingerprint.method.addInstructions(0, """
            invoke-static {}, $getPaidLicenseMethod
            move-result-object v0
            return-object v0
        """.trimIndent())
    }
}