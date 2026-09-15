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

rootProject.name = "OpenWPS"

include(":app")

// core
include(":core:common")
include(":core:ui")
include(":core:database")
include(":core:security")
include(":core:networking")
include(":core:filesystem")
include(":core:logging")

// engines
include(":engines:document")
include(":engines:spreadsheet")
include(":engines:presentation")
include(":engines:pdf")
include(":engines:conversion")
include(":engines:common")

// office
include(":office:model")
include(":office:api")
include(":office:importers")
include(":office:exporters")

// ai
include(":ai:core")
include(":ai:agent")
include(":ai:planner")
include(":ai:tools")
include(":ai:providers")
include(":ai:context")

// image
include(":image:core")
include(":image:providers")

// testing
include(":testing")

include(":core:common")
include(":core:database")
include(":core:filesystem")
include(":core:ui")
include(":engines:common")
