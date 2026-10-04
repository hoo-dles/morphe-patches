package hoodles.morphe.patches.protonvpn.shared.misc.unlockfeatures

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.compatibility.Compat

val unlockFeaturesPatch = bytecodePatch {
    compatibleWith(Compat.PROTON_VPN)

    execute {
        IsPlusMemberFingerprint.method.returnEarly(true)
    }
}