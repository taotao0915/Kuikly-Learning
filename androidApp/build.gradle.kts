plugins {
    id("com.android.application")
    kotlin("android")
}

val kuiklyVersion: String by project

android {
    namespace = "com.example.kuiklylearning"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.kuiklylearning"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        release { isMinifyEnabled = false }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.tencent.kuikly-open:core-render-android:$kuiklyVersion")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
}
