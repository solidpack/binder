plugins {
    `kotlin-dsl`
    alias(libs.plugins.plugin.publish)
}

dependencies {
    api(rootProject.libs.solid)
    implementation(rootProject.libs.kotlinpoet)
}

gradlePlugin {
    plugins {
        create("binderPlugin") {
            website = "https://github.com/solid-resourcepack/binder"
            vcsUrl = "https://github.com/solid-resourcepack/binder"
            id = "io.github.solid-resourcepack.binder"
            displayName = "binder"
            description = "A gradle plugin that generates java/kotlin bindings for minecraft resource packs"
            implementationClass = "io.github.solid.binder.plugin.BinderPlugin"
            tags.set(listOf("generation", "minecraft", "resource pack", "bindings"))
        }
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
}