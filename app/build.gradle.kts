import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.newsapiclient"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.newsapiclient"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        val apiKey: String = project.findProperty("API_KEY") as String
        val baseUrl: String = project.findProperty("BASE_URL") as String
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    ksp(libs.androidx.lifecycle.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Retrofit
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.converter.gson)
    implementation(libs.squareup.logging.interceptor)
    implementation(libs.squareup.okhttp)

    //di
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.squareup.mockwebserver3)
    testImplementation(libs.truth)
    testImplementation(libs.robolectric)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}