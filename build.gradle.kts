buildscript {
    repositories {
        google()
        mavenCentral()
    }
    extra.apply {
        set("hiltVersion", "2.50")
        set("serializationVersion", "1.5.1") // Use the latest stable version

    }
    dependencies {
//        classpath("com.android.tools.build:gradle:${Versions.agp}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}")

    }
    allprojects {
        configurations.all {
            resolutionStrategy.force("com.squareup:javapoet:1.13.0")
        }
    }

}
