import config.AppConfig
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
}

android {
    namespace = AppConfig.ID
    compileSdk = AppConfig.Sdk.COMPILE

    defaultConfig {
        applicationId = AppConfig.ID
        minSdk = AppConfig.Sdk.MIN
        targetSdk = AppConfig.Sdk.TARGET
        versionCode = AppConfig.version.code
        versionName = AppConfig.version.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        viewBinding = true
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
    // Internal modules
    implementation(project(":design-system"))
    implementation(project(":core:network"))
    implementation(project(":core:cachemanager"))

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.appCompat)
    implementation(libs.googleMaterial)
    implementation(libs.constraintLayout)
    implementation(libs.activity)

    // Lifecycle
    implementation(libs.viewModel)
    implementation(libs.liveData)
    implementation(libs.lifecycleRuntime)
    implementation(libs.commonJava8)

    // Compose via BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Compose non-BOM
    implementation(libs.composeLifecycle)
    implementation(libs.composeNavigation)

    // DI - Koin
    implementation(libs.bundles.koinBundle)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.okhttps3Logging)
    implementation(libs.gson)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Utils
    implementation(libs.timber)
    implementation(libs.coil)
    implementation(libs.hawk)

    // Coroutines
    implementation(libs.coroutinesCore)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coreTesting)
    testImplementation(libs.truth)
    testImplementation(libs.coroutinesTest)
    testImplementation(libs.turbineTest)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
