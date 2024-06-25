plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
    id("de.mannodermaus.android-junit5") version "1.10.0.0"
}

android {
    namespace = "com.ardev.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ardev.myapplication"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.glide)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.paging.runtime.ktx)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.mockito:mockito-inline:3.11.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.paging:paging-common:3.1.1")
//    // (Required) Writing and executing Unit Tests on the JUnit Platform
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
//
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
//    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.1")
//    testImplementation("junit:junit:4.13.2")
//    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.7.1")
}