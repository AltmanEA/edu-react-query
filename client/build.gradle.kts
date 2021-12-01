plugins {
    kotlin("js") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
}

group = "ru.altmanea.eduReactQuery"
version = "0.1"

repositories {
    mavenCentral()
    flatDir {
        dirs("$projectDir/../model/build/libs")
    }
}

val kotlinWrappersVersion = "0.0.1-pre.264-kotlin-1.5.31"

fun kotlinw(target: String): String =
    "org.jetbrains.kotlin-wrappers:kotlin-$target"

dependencies {
    implementation("ru.altmanea.edu-react-query:model-js-0.1")
    testImplementation(kotlin("test", "1.5.31"))
    implementation(enforcedPlatform(kotlinw("wrappers-bom:${kotlinWrappersVersion}")))
    implementation(kotlinw("react"))
    implementation(kotlinw("react-dom"))
    implementation(kotlinw("styled"))
    implementation(kotlinw("react-router-dom"))
    implementation(kotlinw("redux"))
    implementation(kotlinw("react-redux"))
    implementation(kotlinw("react-query"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.1")
    implementation(npm("axios", "0.24.0"))
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }
}
