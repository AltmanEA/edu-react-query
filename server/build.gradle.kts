val ktorVersion = "1.6.4"
val kotlinVersion = "1.5.31"
val logbackVersion = "1.2.6"

plugins {
    application
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
}

group = "ru.altmanea.eduReactQuery"
version = "0.1"

application {
    mainClass.set("ru.altmanea.eduReactQuery.AppKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

repositories {
    mavenCentral()
    flatDir {
        dirs("$projectDir/../model/build/libs")
    }
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation("ru.altmanea.edu-react-query:model-jvm-0.1")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}