import org.danilopianini.gradle.mavencentral.JavadocJar
import org.gradle.internal.impldep.org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.GZIPInputStream

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.npm.publish)
    alias(libs.plugins.publishOnCentral)
    alias(libs.plugins.taskTree)
    kotlin("plugin.serialization") version "1.9.21"
}

group = "org.danilopianini"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
                implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.bundles.kotlin.testing.common)
                implementation(libs.bundles.kotest.common)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.yaml:snakeyaml:2.2")
                implementation("com.charleskorn.kaml:kaml:0.55.0")
                implementation("com.opencsv:opencsv:5.9")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.runner.junit5)
            }
        }
        val nativeMain by creating {
            dependsOn(commonMain)
        }
        val nativeTest by creating {
            dependsOn(commonTest)
        }
    }

    js(IR) {
        browser()
        nodejs()
        binaries.library()
    }

    val nativeSetup: KotlinNativeTarget.() -> Unit = {
        compilations["main"].defaultSourceSet.dependsOn(kotlin.sourceSets["nativeMain"])
        compilations["test"].defaultSourceSet.dependsOn(kotlin.sourceSets["nativeTest"])
        binaries {
            executable()
            sharedLib()
            staticLib()
        }
    }

    applyDefaultHierarchyTemplate()
    /*
     * Linux 64
     */
    linuxX64(nativeSetup)
    linuxArm64(nativeSetup)
    /*
     * Win 64
     */
    mingwX64(nativeSetup)
    /*
     * Apple OSs
     */
    macosX64(nativeSetup)
    macosArm64(nativeSetup)

    targets.all {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
                freeCompilerArgs += listOf("-Xexpect-actual-classes")
            }
        }
    }

    val os = OperatingSystem.current()
    val excludeTargets = when {
        os.isLinux -> kotlin.targets.filterNot { "linux" in it.name }
        os.isWindows -> kotlin.targets.filterNot { "mingw" in it.name }
        os.isMacOsX -> kotlin.targets.filter { "linux" in it.name || "mingw" in it.name }
        else -> emptyList()
    }.mapNotNull { it as? KotlinNativeTarget }

    configure(excludeTargets) {
        compilations.configureEach {
            cinterops.configureEach { tasks[interopProcessingTaskName].enabled = false }
            compileTaskProvider.get().enabled = false
            tasks[processResourcesTaskName].enabled = false
        }
        binaries.configureEach { linkTask.enabled = false }

        mavenPublication {
            tasks.withType<AbstractPublishToMaven>().configureEach {
                onlyIf { publication != this@mavenPublication }
            }
            tasks.withType<GenerateModuleMetadata>().configureEach {
                onlyIf { publication.get() != this@mavenPublication }
            }
        }
    }
}

tasks.dokkaJavadoc {
    enabled = false
}

tasks.withType<JavadocJar>().configureEach {
    val dokka = tasks.dokkaHtml.get()
    dependsOn(dokka)
    from(dokka.outputDirectory)
}

signing {
    if (System.getenv("CI") == "true") {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
}

publishOnCentral {
    projectLongName.set("Template for Kotlin Multiplatform Project")
    projectDescription.set("A template repository for Kotlin Multiplatform projects")
    repository("https://maven.pkg.github.com/danysk/${rootProject.name}".lowercase()) {
        user.set("DanySK")
        password.set(System.getenv("GITHUB_TOKEN"))
    }
    publishing {
        publications {
            withType<MavenPublication> {
                pom {
                    developers {
                        developer {
                            name.set("Danilo Pianini")
                            email.set("danilo.pianini@gmail.com")
                            url.set("http://www.danilopianini.org/")
                        }
                    }
                }
            }
        }
    }
}

npmPublish {
    registries {
        register("npmjs") {
            uri.set("https://registry.npmjs.org")
            val npmToken: String? by project
            authToken.set(npmToken)
            dry.set(npmToken.isNullOrBlank())
        }
    }
}

publishing {
    publications {
        publications.withType<MavenPublication>().configureEach {
            if ("OSSRH" !in name) {
                artifact(tasks.javadocJar)
            }
        }
    }
}

tasks.register("getNetLogo", DownloadNetLogoTask::class)

open class DownloadNetLogoTask : DefaultTask() {

    @Input
    val netLogoVersion = "6.2.0" // Change this to the desired NetLogo version

    @Input
    val netLogoUrl = "https://ccl.northwestern.edu/netlogo/download/6.2.0/NetLogo-${netLogoVersion}-64.tgz"

    init {
        group = "custom"
        description = "Download NetLogo and unpack files"
    }

    @TaskAction
    fun downloadAndUnpack() {
        val osName = System.getProperty("os.name").lowercase()

        val archiveExtension = when {
            osName.contains("nix") || osName.contains("nux") || osName.contains("mac") -> "tgz"
            else -> throw UnsupportedOperationException("Unsupported operating system: $osName")
        }

        val archiveFileName = "NetLogo-${netLogoVersion}-64.$archiveExtension"
        val archivePath = Paths.get(project.rootDir.toString(), archiveFileName)

        val netLogoDir = Paths.get(project.rootDir.toString(), "NetLogo-${netLogoVersion}")

        // Download NetLogo
        URL(netLogoUrl).openStream().use { input ->
            Files.copy(input, archivePath)
        }

        // Unpack NetLogo
        when {
            osName.contains("nix") || osName.contains("nux") || osName.contains("mac") -> {
                Files.createDirectories(netLogoDir)
                Files.newInputStream(archivePath).use { input ->
                    GZIPInputStream(input).use { gzipInput ->
                        TarArchiveInputStream(gzipInput).use { tarInput ->
                            var entry = tarInput.nextTarEntry
                            while (entry != null) {
                                val entryPath = netLogoDir.resolve(entry.name)
                                if (entry.isDirectory) {
                                    Files.createDirectories(entryPath)
                                } else {
                                    Files.newOutputStream(entryPath).use { output ->
                                        tarInput.copyTo(output)
                                    }
                                }
                                entry = tarInput.nextTarEntry
                            }
                        }
                    }
                }
            }
            else -> throw UnsupportedOperationException("Unsupported operating system: $osName")
        }

        // Delete the compressed archive
        Files.delete(archivePath)
    }
}