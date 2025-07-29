plugins {
    id("io.github.solid-resourcepack.binder") version "1.1.6"
}

sourceSets.main {
    kotlin.srcDir("build/generated")
}

dependencies {
    compileOnly(rootProject.libs.solid)
}

packBinder {
    packPath.from(layout.projectDirectory.dir("pack-sample"))
    nameDepth = 1
    namespaces.add("example")
    dest.set(layout.buildDirectory.dir("generated"))
}