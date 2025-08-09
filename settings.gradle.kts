pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// 依赖project时提供类型安全的访问器
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "android-base-ui-ktx"
include(":app")
include(":base-ui")
