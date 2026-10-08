/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.alltrails.heatmap

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringOption
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import hoodles.morphe.patches.alltrails.shared.Constants
import java.net.URI
import java.util.logging.Logger

private fun isValidHeatmapTileUrlTemplate(value: String): Boolean {
    if (!value.startsWith("https://") && !value.startsWith("http://")) {
        return false
    }
    if (!value.contains("{z}") || !value.contains("{x}") || !value.contains("{y}")) {
        return false
    }
    return try {
        val uri = URI(value.replace("{z}", "0").replace("{x}", "0").replace("{y}", "0"))
        !uri.host.isNullOrBlank()
    } catch (_: Exception) {
        false
    }
}

private fun MutableMethod.overwriteHeatmapUrlAfterToString(proxyUrl: String): Boolean {
    val instructions = implementation?.instructions ?: return false
    var replaced = false

    val toStringMoveResultIndices = mutableListOf<Int>()
    for (index in instructions.indices) {
        val instruction = instructions[index]
        if (instruction.opcode != Opcode.INVOKE_VIRTUAL) continue

        val reference = (instruction as? ReferenceInstruction)?.reference as? MethodReference
            ?: continue
        if (reference.definingClass != "Ljava/lang/StringBuilder;") continue
        if (reference.name != "toString") continue

        val moveResultIndex = index + 1
        if (moveResultIndex >= instructions.size) continue
        if (instructions[moveResultIndex].opcode != Opcode.MOVE_RESULT_OBJECT) continue

        toStringMoveResultIndices += moveResultIndex
    }

    for (moveResultIndex in toStringMoveResultIndices.asReversed()) {
        val register = getInstruction<OneRegisterInstruction>(moveResultIndex).registerA
        addInstructions(
            moveResultIndex + 1,
            """
            const-string v$register, "$proxyUrl"
            """.trimIndent()
        )
        replaced = true
    }

    return replaced
}

@Suppress("unused")
val proxyHeatmapPatch = bytecodePatch(
    name = "Proxy heatmap tiles",
    description = "Optional. Rewrites AllTrails community heatmap tile URL templates to a " +
        "configurable proxy (default Tailscale host). " +
        "Off by default — enable explicitly. Unrelated AllTrails API hosts are left unchanged.",
    default = false
) {
    compatibleWith(Constants.COMPATIBILITY)

    val heatmapTileUrlTemplate by stringOption(
        key = "heatmapTileUrlTemplate",
        default = DEFAULT_HEATMAP_TILE_URL_TEMPLATE,
        title = "Heatmap tile URL template",
        description = "Full tile URL template including {z}/{x}/{y} placeholders. " +
            "Example: https://tiles.example.ts.net/tiles/years_1/all/{z}/{x}/{y}.png",
        required = true,
        validator = { value ->
            value != null && isValidHeatmapTileUrlTemplate(value)
        }
    )

    execute {
        val logger = Logger.getLogger("ProxyHeatmapPatch")
        val proxyUrl = heatmapTileUrlTemplate
            ?: error("Heatmap tile URL template is required")

        if (!isValidHeatmapTileUrlTemplate(proxyUrl)) {
            error(
                "Invalid heatmap tile URL template. Provide an http(s) URL containing {z}, {x}, and {y}."
            )
        }

        if ('"' in proxyUrl || '\\' in proxyUrl) {
            error("Heatmap tile URL template must not contain quotes or backslashes")
        }

        fun applyProxyUrl(fingerprint: Fingerprint, label: String) {
            val matches = fingerprint.matchAllOrNull()
            if (matches.isNullOrEmpty()) {
                logger.warning("No matches for $label; skipping")
                return
            }

            var applied = 0
            matches.forEach { match ->
                if (match.method.overwriteHeatmapUrlAfterToString(proxyUrl)) {
                    applied++
                } else {
                    logger.warning(
                        "Matched $label in ${match.method} but could not overwrite StringBuilder result"
                    )
                }
            }
            logger.info("Proxy heatmap: applied $label to $applied method(s)")
        }

        applyProxyUrl(HeatmapTilePathFingerprint, "heatmap path builder")
        applyProxyUrl(HeatmapTileHostSuffixFingerprint, "heatmap host-suffix builder")
    }
}
