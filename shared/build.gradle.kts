plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.google.devtools.ksp")
}

val kuiklyVersion: String by project

kotlin {
    // 第一阶段在 Windows 上学习，先启用 Android。
    // 页面仍放在 commonMain；以后扩展其他平台时可复用这里的代码。
    androidTarget {
        compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) }
    }

    sourceSets {
        commonMain.dependencies {
            api("com.tencent.kuikly-open:core:$kuiklyVersion")
            implementation("com.tencent.kuikly-open:core-annotations:$kuiklyVersion")
        }
    }
}

android {
    namespace = "com.example.kuiklylearning.shared"
    compileSdk = 35
    defaultConfig { minSdk = 23 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // 扫描 @Page，自动生成 Android 渲染器需要的页面入口。
    add("kspAndroid", "com.tencent.kuikly-open:core-ksp:$kuiklyVersion")
}
