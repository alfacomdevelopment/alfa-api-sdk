import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.freefair.lombok)
    alias(libs.plugins.openapi.generator)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val openApiSpecsDir = layout.projectDirectory.dir("src/main/resources/openapi")
val openApiOutDir = layout.buildDirectory.dir("generated/openapi")

fun GenerateTask.configureCommon(specFileName: String) {
    group = "openapi"

    generatorName.set("java")
    inputSpec.set(openApiSpecsDir.file(specFileName).asFile.absolutePath)
    outputDir.set(openApiOutDir.get().asFile.absolutePath)

    globalProperties.set(
        mapOf(
            "models" to "",
            "apis" to "false",
            "supportingFiles" to "false",
            "modelDocs" to "false",
            "modelTests" to "false",
            "apiDocs" to "false",
            "apiTests" to "false"
        )
    )

    configOptions.set(
        mapOf(
            "library" to "webclient",
            "dateLibrary" to "java8",
            "hideGenerationTimestamp" to "true",
            "serializableModel" to "true",
            "useJakartaEe" to "false"
        )
    )
}

val openApiGenerateModelsCustomerInfo by tasks.registering(GenerateTask::class) {
    description = "Generate models from сustomer-info-v2.yaml"
    configureCommon("сustomer-info-v2.yaml")
    modelPackage.set("com.alfa.api.sdk.customer.info.v2.generated.model")
}

sourceSets["main"].java.srcDir(openApiOutDir.map { it.dir("src/main/java") })

tasks.named("compileJava") {
    dependsOn(openApiGenerateModelsCustomerInfo)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)

    dependsOn(openApiGenerateModelsCustomerInfo)
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
    api(project(":api-sdk-core"))

    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.databind)
    implementation(libs.jackson.dataformat.xml)
    implementation(libs.jackson.jaxb.annotations)
    implementation(libs.jackson.datatype.jsr310)

    implementation(libs.javax.jaxb.api)
    compileOnly(libs.javax.annotation.api)
    compileOnly(libs.jsr305)
    compileOnly(libs.threeten.jaxb.core)
    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)
}
