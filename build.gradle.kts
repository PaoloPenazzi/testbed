@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.dokka)
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.publishOnCentral)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.taskTree)
    kotlin("plugin.serialization") version "1.9.21"
}

application {
    mainClass = "Main"
}

group = "io.github.paolopenazzi"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.bundles.kotlin.testing)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("com.charleskorn.kaml:kaml:0.55.0")
    implementation("com.opencsv:opencsv:5.9")
}

kotlin {
    target {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
                freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        showCauses = true
        showStackTraces = true
        events(*org.gradle.api.tasks.testing.logging.TestLogEvent.values())
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}

publishOnCentral {
    projectLongName.set("Testbed")
    projectDescription.set("An open benchmarking platform for Collective Adaptive Systems")
    repository("https://maven.pkg.github.com/paolopenazzi/${rootProject.name}".lowercase()) {
        user.set("paolopenazzi")
        password.set(System.getenv("GH_TOKEN"))
    }
    publishing {
        publications {
            withType<MavenPublication> {
                pom {
                    developers {
                        developer {
                            id.set("paolopenazzi")
                            name.set("Paolo Penazzi")
                            email.set("paolo.penazzi@studio.unibo.it")
                        }
                    }
                }
            }
        }
    }
}

tasks {
    val jar = register<Jar>("fatJar") {
        dependsOn.addAll(
            listOf(
                "compileJava",
                "compileKotlin",
                "processResources",
            ),
        ) // We need this for Gradle optimization to work
        archiveFileName.set("testbed-" + rootProject.version.toString() + ".jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to application.mainClass,
                ),
            )
        } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } + sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(jar) // Trigger jar creation during build
    }
}

