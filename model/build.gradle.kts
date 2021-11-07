plugins {
    kotlin("multiplatform") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
}

val kotlinVersion = "1.5.31"
val serializationVersion = "1.3.0"

group = "ru.altmanea.edu-react-query"
version = "0.1"

repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }
    js {
        browser {
            binaries.executable()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}