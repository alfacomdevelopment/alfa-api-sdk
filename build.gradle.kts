plugins {
    id("pl.allegro.tech.build.axion-release") version "1.21.1"
}

allprojects {
    group = "com.alfa.api.sdk"
    project.version = rootProject.version

    repositories {
        mavenCentral()
    }
}

scmVersion {
    tag {
        prefix = "v"
    }
    releaseOnlyOnReleaseBranches = true
    releaseBranchNames = listOf("main")
}


version = scmVersion.version
