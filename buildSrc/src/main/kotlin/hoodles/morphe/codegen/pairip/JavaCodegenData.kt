/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.codegen.pairip

import kotlinx.serialization.Serializable

@Serializable
data class JavaField(val name: String, val value: String)

@Serializable
data class JavaCodegenData(
    val strings: Map<String, List<JavaField>>,
    val methods: Map<String, List<JavaField>>
)
