package io.github.solid.binder.api

import com.squareup.kotlinpoet.*
import io.github.solid.resourcepack.api.link.ModelType
import net.kyori.adventure.key.Key
import java.nio.file.Path

class EnumGenerator(
    private val models: List<PackModel>,
    private val name: String,
    private val dest: Path,
    private val packageName: String,
    private val depth: Int,
) {
    fun generate() {
        val modelKey = "model"
        val modelEnum = TypeSpec.enumBuilder(name)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(modelKey, typeNameOf<PackModel>())
                    .build()
            )
            .addProperty(
                PropertySpec.builder(modelKey, typeNameOf<PackModel>())
                    .initializer(modelKey)
                    .build()
            )
        models.forEach { model ->
            val enumName = model.key.value()
                .uppercase()
                .split("/")
                .reversed()
                .subList(0, depth)
                .joinToString("_")
                .replace("-", "_")
            val block = CodeBlock.builder()
            block.add("%T(", PackModel::class)
            block.add("key = %T.key(%S)", Key::class, model.key)
            block.add(", type = %T.${model.type}", ModelType::class)
            if (model.parent != null) {
                block.add(", parent = %T.key(%S)", Key::class, model.parent)
            }
            if (model.predicates != null) {
                block.add(", predicates = mapOf(")
                model.predicates.onEachIndexed { index, entry ->
                    block.add("%S to %S", entry.key, entry.value)
                    if (model.predicates.size > index + 1) {
                        block.add(", ")
                    }
                }
                block.add(")")
            }

            block.add(")")
            modelEnum.addEnumConstant(
                enumName, TypeSpec.anonymousClassBuilder().addSuperclassConstructorParameter(block.build()).build()
            )
        }

        val file = FileSpec.builder(packageName, name).addType(
            modelEnum.build()
        )
        file.build().writeTo(dest)
    }
}