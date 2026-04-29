pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

rootProject.name = "module-platform"

// 框架
includeBuild("../neton")

// 主应用（提供 module-system）
includeBuild("../privchat-application")
