# binder ![Maven Central Version](https://img.shields.io/maven-central/v/io.github.solid-resourcepack.binder/api) ![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/io.github.solid-resourcepack.binder)

> [!CAUTION]
> The gradle plugin is not yet accepted in the plugin portal!

A gradle plugin that generates bindings for blocks and items of a specified minecraft resource pack.
This aims to create a typesafe experience through enum classes when creating everything related to resource packs
(see [example implementation](/test-plugin)).

## Setup

```kotlin
// build.gradle.kts

plugins {
    id("io.github.solid-resourcepack.binder") version "VERSION"
}

sourceSets.main {
    kotlin.srcDir("build/generated") //Include the destination dir
}

dependencies {
    api("io.github.solid-resourcepack.binder:api:VERSION")
}

packBinder {
    packPath.from(layout.projectDirectory.dir("pack-sample")) //Define paths where your resource packs are
    nameDepth = 1 //How much depth of the model namespace should be included
    namespaces.add("example") // Add namespaces you want to generate
    dest.set(layout.buildDirectory.dir("generated")) //Set the destination dir
    packageName.set("com.example.generated") //Set the package of the generated classes
    className.set("PackBindings") //Set the class name of the resulting enum
}
```

