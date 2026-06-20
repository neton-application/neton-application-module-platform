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

// Neton base shell（提供 module-system）
// canonicalize：统一用 ../../Neton/ 前缀指向 Neton canonical 工作区（跨工作区一致）
includeBuild("../../Neton/neton-application")
