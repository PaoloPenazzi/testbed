plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        resources {
            srcDir("src/main/protelis")
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.yaml:snakeyaml:2.2")
    implementation("com.charleskorn.kaml:kaml:0.55.0")
    implementation("it.unibo.alchemist:alchemist:28.5.4")
    implementation("it.unibo.alchemist:alchemist-incarnation-protelis:28.5.4")
    implementation("it.unibo.alchemist:alchemist-incarnation-sapere:28.5.4")
    implementation("it.unibo.alchemist:alchemist-incarnation-scafi:28.5.4")
    implementation("it.unibo.alchemist:alchemist-swingui:29.0.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}
