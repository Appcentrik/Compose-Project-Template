@file:Suppress("UnstableApiUsage")

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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "CPT"
include(":app")
include(":common")
include(":commonAndroid")
include(":data:local")
include(":data:remote")
include(":data:repository")
include(":data:session")
include(":data:source")
include(":domain:gateway")
include(":domain:model")
include(":domain:useCase")
include(":navigation")
include(":feature:home")
include(":feature:launcher")
include(":feature:articledetail")
include(":feature:webscreen")
