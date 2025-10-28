plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    kotlin("plugin.serialization") version "1.9.0"
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.sample.tidbooktimer"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.sample.tidbooktimer"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)
    implementation(libs.dagger.hilt.navigation.compose)
    implementation(libs.coil)
    implementation(libs.androidx.datastore.preferences)

    // Firebase BOM controls all Firebase versions
    implementation(platform(libs.firebase.bom))
    // Firebase libraries
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
}