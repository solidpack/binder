package io.github.solid.binder.api

import io.github.solid.resourcepack.api.link.ModelLinkCollector
import net.kyori.adventure.key.Key
import java.nio.file.Path

object PackCrawler {

    fun crawl(path: Path, namespaces: List<String> = listOf(Key.MINECRAFT_NAMESPACE)): List<PackModel> {
        return ModelLinkCollector(path).collect().filter { namespaces.contains(it.key.namespace()) }.map {
            PackModel(it.key, it.modelType, it.parent, it.predicates)
        }
    }
}