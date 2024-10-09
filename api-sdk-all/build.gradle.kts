version = "0.3.2"

plugins{
    `java-library`
    `maven-publish`
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/alfacomdevelopment/alfa-api-sdk")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

dependencies {
    api(project(":api-sdk-transactions"))
    api(project(":api-sdk-crypto"))
}