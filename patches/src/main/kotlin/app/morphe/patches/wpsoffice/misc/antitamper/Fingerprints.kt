package app.morphe.patches.wpsoffice.misc.antitamper

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object SecurityCheck1Fingerprint : Fingerprint(
    parameters = listOf(),
    returnType = "V",
    filters = listOf(
        methodCall(
            definingClass = "Lcn/wps/moffice/define/VersionManager;",
            opcode = Opcode.INVOKE_STATIC
        ),
        string("info.r")
    ),
    custom = { _, classDef -> classDef.startsWith("Lcn/wps/moffice/main/sec")}
)

object SecurityCheck2Fingerprint : Fingerprint(
    parameters = listOf(),
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    filters = listOf(
        methodCall(
            definingClass = "Lcn/wps/moffice/define/VersionManager;",
            opcode = Opcode.INVOKE_STATIC
        ),
        methodCall(
            definingClass = "Landroid/os/Process;",
            name = "killProcess"
        )
    )
)