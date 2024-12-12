pluginManagement {
    repositories {
        google() // Add Google repository
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // Add Google repository
        mavenCentral()
    }
}

rootProject.name = "cOrAL"
include(":app")
