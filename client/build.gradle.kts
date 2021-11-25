plugins {
    kotlin("js") version "1.5.31"
}

group = "ru.altmanea.eduReactQuery"
version = "0.1"

repositories {
    mavenCentral()
}

val kotlinWrappersVersion = "0.0.1-pre.264-kotlin-1.5.31"

fun kotlinw(target: String): String =
    "org.jetbrains.kotlin-wrappers:kotlin-$target"

dependencies {
    testImplementation(kotlin("test", "1.5.31"))
    implementation(enforcedPlatform(kotlinw("wrappers-bom:${kotlinWrappersVersion}")))
    implementation(kotlinw("react"))
    implementation(kotlinw("react-dom"))
    implementation(kotlinw("styled"))
    implementation(kotlinw("react-router-dom"))
    implementation(kotlinw("redux"))
    implementation(kotlinw("react-redux"))
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
