package io.github.solid.binder.api

import io.github.solid.resourcepack.api.link.ModelType
import net.kyori.adventure.key.Key

data class PackModel(
    val key: Key,
    val type: ModelType,
    val parent: Key? = null,
    val predicates: Map<String, Any>? = null,
)