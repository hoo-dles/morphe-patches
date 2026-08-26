package hoodles.morphe.util

import app.morphe.patcher.patch.ApkArchitecture
import app.morphe.patcher.patch.AvailabilityResolver
import app.morphe.patcher.patch.InstallerType
import app.morphe.patcher.patch.PatchAvailability

fun requireArm64(default: Boolean = true) = AvailabilityResolver {
    _, architecture -> when (architecture) {
        ApkArchitecture.ARM64_V8A -> if (default) PatchAvailability.ENABLED else PatchAvailability.DISABLED
        else -> PatchAvailability.UNAVAILABLE
    }
}

val requireRootMount = AvailabilityResolver {
    type, _ -> when (type) {
        InstallerType.MOUNT -> PatchAvailability.ENABLED
        else -> PatchAvailability.UNAVAILABLE
    }
}

fun combine(vararg resolvers: AvailabilityResolver) = AvailabilityResolver {
    type, architecture -> resolvers.map { it.resolve(type, architecture) }.maxBy { it.ordinal }
}