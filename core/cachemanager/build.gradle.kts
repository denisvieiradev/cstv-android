import config.AppConfig
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.denisvieiradev.cachemanager"
    compileSdk = AppConfig.Sdk.COMPILE

    defaultConfig {
        minSdk = AppConfig.Sdk.MIN
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(libs.bundles.koinBundle)
    implementation(libs.hawk)
    implementation(libs.androidx.security.crypto)
}
