plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
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
    implementation("com.charleskorn.kaml:kaml:0.55.0")
    implementation("it.unibo.alchemist:alchemist:28.4.3")
    implementation("it.unibo.alchemist:alchemist-incarnation-protelis:28.4.3")
    implementation("it.unibo.alchemist:alchemist-incarnation-sapere:28.4.3")
    implementation("it.unibo.alchemist:alchemist-incarnation-scafi:28.4.2")
    implementation("it.unibo.alchemist:alchemist-swingui:28.4.2")
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
