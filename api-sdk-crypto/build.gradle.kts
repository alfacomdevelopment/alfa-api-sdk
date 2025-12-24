import io.freefair.gradle.plugins.lombok.tasks.Delombok

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.freefair.lombok)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val delombokSourcesDir = "${layout.buildDirectory.get()}/delombokSources"
tasks.named<Delombok>("delombok") {
    input.setFrom(sourceSets["main"].java.srcDirs)
    target.set(file(delombokSourcesDir))
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(delombokSourcesDir)
}

tasks.named("sourcesJar") {
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
    implementation(libs.bcprov.jdk18on)
    implementation(libs.bcpkix.jdk18on)
    implementation(libs.nimbus.jose.jwt)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}