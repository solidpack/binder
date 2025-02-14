package io.github.solid.binder.api

import net.kyori.adventure.key.Key
import team.unnamed.creative.model.ItemPredicate

data class PackModel(
    val key: Key,
    val predicates: List<ItemPredicate>? = null,
)