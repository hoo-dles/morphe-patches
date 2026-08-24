package hoodles.morphe.patches.shared.misc.pairip.native

import kotlinx.serialization.Serializable

@Serializable
data class RelocationEntry(val address: Long, val symbol: String)

@Serializable
data class LibPatchInfo(
    val keystream: String,
    val relocations: List<RelocationEntry>
)