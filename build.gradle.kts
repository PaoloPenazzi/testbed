import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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
    alias(libs.plugins.shadowJar)
    kotlin("plugin.serialization") version "2.1.10"
}

application {
    mainClass = "testbed.TestbedKt"
}

group = "io.github.paolopenazzi"

repositories {
    mavenCentral()
}

tasks.withType<ShadowJar> {
    archiveFileName.set("testbed.jar")
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to "Testbed",
                "Implementation-Version" to rootProject.version.toString(),
                "Main-Class" to "testbed.Testbed",
            ),
        )
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.bundles.kotlin.testing)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.yaml:snakeyaml:2.4")
    implementation("com.charleskorn.kaml:kaml:0.74.0")
    implementation("com.opencsv:opencsv:5.10")
    implementation(kotlin("script-runtime"))
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
