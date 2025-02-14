package io.github.solid.binder.api

import net.kyori.adventure.key.Key
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.model.ItemPredicate
import team.unnamed.creative.model.Model
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader
import java.nio.file.Path
import kotlin.io.path.isDirectory

object PackCrawler {

    fun crawl(path: Path, namespaces: List<String> = listOf(Key.MINECRAFT_NAMESPACE)): List<PackModel> {
        val pack: ResourcePack = if (path.isDirectory()) {
            MinecraftResourcePackReader.minecraft().readFromDirectory(path.toFile())
        } else {
            MinecraftResourcePackReader.minecraft().readFromZipFile(path)
        }
        return pack.models().filter { namespaces.contains(it.key().namespace()) }.map { model ->
            PackModel(key = model.key(), predicates = findPredicates(pack, model))
        }
    }

    private fun findPredicates(pack: ResourcePack, model: Model): List<ItemPredicate>? {
        pack.models().filter { it.key().namespace() == Key.MINECRAFT_NAMESPACE && it.key() != model.key() }
            .forEach { parent ->
                val result = parent.overrides().firstOrNull { it.model().equals(model.key()) }?.predicate()
                if (result != null) return result
            }
        return null
    }
}