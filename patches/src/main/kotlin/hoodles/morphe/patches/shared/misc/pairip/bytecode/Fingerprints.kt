/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.shared.misc.pairip.bytecode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall

object VMRunnerStaticCtorFingerprint : Fingerprint (
    definingClass = "Lcom/pairip/VMRunner;",
    name = "<clinit>"
)

object VMRunnerInvokeFingerprint : Fingerprint (
    definingClass = "Lcom/pairip/VMRunner;",
    name = "invoke"
)

object StartupLaunchFingerprint : Fingerprint (
    definingClass = "Lcom/pairip/StartupLauncher;",
    name = "launch",
    filters = listOf(
        methodCall(name = "invoke")
    )
)