import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.central.publish)
    `maven-publish`
}

allprojects {

    group = "io.github.solid-resourcepack.binder"
    version = "1.1.6"

    repositories {
        mavenCentral()
        maven("https://repo.mcsports.club/snapshots")
    }
}

subprojects {
    apply {
        plugin(rootProject.libs.plugins.kotlin.get().pluginId)
        plugin(rootProject.libs.plugins.shadow.get().pluginId)
        plugin(rootProject.libs.plugins.central.publish.get().pluginId)
        plugin("maven-publish")
    }

    dependencies {
        testImplementation(rootProject.libs.kotlin.test)
        implementation(rootProject.libs.kotlin.jvm)
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    kotlin {
        jvmToolchain(21)
        compilerOptions {
            apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    tasks.shadowJar {
        mergeServiceFiles()
        archiveFileName.set("${project.name}.jar")
    }

    publishing {
        if (project.name != "api") return@publishing
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])

            }
        }
    }

    centralPortal {
        name = project.name

        pom {
            name.set("Binder")
            description.set("A gradle plugin that generates java/kotlin bindings for minecraft resource packs")
            url.set("https://github.com/solid-resourcepack/binder")

            developers {
                developer {
                    id.set("dayyeeet")
                    email.set("david@cappell.net")
                }
            }
            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            scm {
                url.set("https://github.com/solid-resourcepack/binder")
                connection.set("git:git@github.com:solid-resourcepack/binder.git")
            }
        }
    }

    signing {
        useGpgCmd()
        sign(publishing.publications)
    }

    tasks.test {
        useJUnitPlatform()
    }
}