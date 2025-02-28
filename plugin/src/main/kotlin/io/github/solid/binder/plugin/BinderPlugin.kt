package io.github.solid.binder.plugin

import io.github.solid.resourcepack.api.link.ModelLink
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import javax.inject.Inject


abstract class GenerateBindingsTask : DefaultTask() {

    @get:Input
    abstract val className: Property<String>

    @get:Input
    abstract val nameDepth: Property<Int>

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val namespaces: ListProperty<String>

    @get:InputFiles
    abstract val packPath: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val dest: DirectoryProperty

    @TaskAction
    fun action() {
        val models = mutableListOf<ModelLink>()
        packPath.files.forEach { from ->
            models.addAll(PackCrawler.crawl(from.toPath(), namespaces.get()))
        }
        val generator =
            EnumGenerator(
                models = models,
                name = className.get(),
                dest = dest.get().asFile.toPath(),
                depth = nameDepth.get(),
                packageName = packageName.get()
            )
        generator.generate()
    }
}

open class BinderPluginExtension @Inject constructor(objects: ObjectFactory) {

    val className: Property<String> = objects.property(String::class.java).convention("PackBindings")
    val nameDepth: Property<Int> = objects.property(Int::class.java).convention(2)
    val packageName: Property<String> = objects.property(String::class.java).convention("com.example.generated")
    val namespaces: ListProperty<String> = objects.listProperty(String::class.java).convention(listOf("example"))
    val packPath: ConfigurableFileCollection = objects.fileCollection()
    val dest: DirectoryProperty =
        objects.directoryProperty().convention(objects.directoryProperty().dir("build/generated"))
}

abstract class BinderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("packBinder", BinderPluginExtension::class.java)
        project.tasks.register<GenerateBindingsTask>("generatePackBindings") {
            className.set(extension.className)
            nameDepth.set(extension.nameDepth)
            packageName.set(extension.packageName)
            namespaces.set(extension.namespaces)
            packPath.from(extension.packPath)
            dest.set(extension.dest)
        }
    }
}