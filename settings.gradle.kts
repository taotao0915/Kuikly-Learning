pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Kuikly 由腾讯官方 Maven 仓库提供。
        maven("https://mirrors.tencent.com/repository/maven-tencent/") {
            content { includeGroup("com.tencent.kuikly-open") }
        }
    }
}

rootProject.name = "KuiklyLearning"
include(":shared", ":androidApp")
