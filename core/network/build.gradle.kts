import config.AppConfig
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.denisvieiradev.network"
    compileSdk = AppConfig.Sdk.COMPILE

    defaultConfig {
        minSdk = AppConfig.Sdk.MIN
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(libs.bundles.koinBundle)
    api(libs.retrofit)
    api(libs.retrofitConverterGson)
    implementation(libs.timber)
    api(libs.okhttps3Logging)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
