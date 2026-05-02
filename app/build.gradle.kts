import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serializer)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.mayurdw.fibretracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mayurdw.fibretracker"
        minSdk = 29
        targetSdk = 36
        versionCode = 8
        versionName = "0.71"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

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
    kotlin {
        sourceSets.all {
            languageSettings.enableLanguageFeature("ExplicitBackingFields")
        }

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.room)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.compose.foundation)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt)
    implementation(libs.hilt.navigation)
    implementation(libs.datetime)
    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.room.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
