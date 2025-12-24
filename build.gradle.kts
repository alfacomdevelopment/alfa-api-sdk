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
}

version = scmVersion.version

allprojects {
    group = "com.alfa.api.sdk"
    project.version = rootProject.version

    repositories {
        mavenCentral()
    }
}
