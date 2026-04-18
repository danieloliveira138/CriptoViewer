plugins {
    id("com.android.application") version "9.1.1" apply false
    id("org.jetbrains.kotlin.android") version "2.3.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.20" apply false
    id("com.google.dagger.hilt.android") version "2.59.2" apply false
    id("com.google.devtools.ksp") version "2.1.10-1.0.30" apply false
    kotlin("jvm") version "2.1.10" apply false
    kotlin("plugin.serialization") version "2.1.10" apply false
}

buildscript {
    dependencies {
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.3.5")
    }
}