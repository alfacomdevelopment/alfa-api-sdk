import io.freefair.gradle.plugins.lombok.tasks.Delombok

version = "0.2.1"

plugins {
    java
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "8.4"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val delombokSourcesDir = "${layout.buildDirectory.get()}/delombokSources"
tasks.named<Delombok>("delombok") {
    input.setFrom(files("src/main/java"))
    target.set(file(delombokSourcesDir))
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(delombokSourcesDir)
}
tasks.named("sourcesJar") {
    dependsOn("delombok")
}
tasks.named("compileJava") {
    dependsOn("delombok")
}

tasks.withType<Checkstyle>().configureEach {
    sourceSets["main"].java.setSrcDirs(listOf("src/main/java"))
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:-missing", "-quiet")
}

tasks.named("build").configure {
    dependsOn("sourcesJar")
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
            artifact(tasks["sourcesJar"])
        }
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}