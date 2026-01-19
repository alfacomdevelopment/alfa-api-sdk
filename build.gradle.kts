plugins {
    id("pl.allegro.tech.build.axion-release") version "1.21.1"
}
apply(plugin = "pl.allegro.tech.build.axion-release")

scmVersion {
    tag {
        prefix = "v"
    }
    releaseOnlyOnReleaseBranches = true
    releaseBranchNames = listOf("main")
    val releaseBranches = releaseBranchNames.orNull ?: emptyList()
    versionCreator { version, position ->
        val base = version.toString()
        if (position.branch != null && releaseBranches.contains(position.branch)) {
            base
        } else {
            val buildNumber = providers.environmentVariable("GITHUB_RUN_NUMBER").orNull ?: "0"
            "$base-$buildNumber"
        }
    }
}

version = scmVersion.version

allprojects {
    group = "com.alfa.api.sdk"
    project.version = rootProject.version

    repositories {
        mavenCentral()
    }
}
