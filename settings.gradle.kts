pluginManagement {
    repositories {
        google()
        mavenCentral()

        gradlePluginPortal()
        maven { setUrl("https://jitpack.io") }

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url =uri("http://maven.andob.info/repository/open_source")
            isAllowInsecureProtocol = true
        }
    }







}

rootProject.name = "Photos to PDF"
include(":app")
 