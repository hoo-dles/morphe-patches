package app.morphe.patches.materialcapsule.pro

import app.morphe.patcher.Fingerprint

object DecryptFingerprint : Fingerprint (
    definingClass = "DataStoreRepositoryBilling;",
    name = "decrypt"
)