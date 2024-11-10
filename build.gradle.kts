// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.kapt") version "2.0.21" apply false // kotlin annotation processing
    id("com.google.devtools.ksp") version "2.0.21-1.0.25" apply false // kotlin symbol processing
}