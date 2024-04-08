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
    }
}

rootProject.name = "MultiCurrency"
include(":app")
include(":shared")
include(":shared:core-network")
include(":shared:core-logger")
include(":shared:feature-currency-converter")
include(":shared:core-base")
include(":shared:core-preferences")
