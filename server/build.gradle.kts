plugins {
    kotlin("jvm") version "1.5.31"
}

group = "ru.altmanea.edu-react-query"
version = "0.1"

repositories {
    mavenCentral()
    flatDir {
        dirs("$projectDir/../model/build/libs")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("ru.altmanea.edu-react-query:model-jvm-0.1")
}