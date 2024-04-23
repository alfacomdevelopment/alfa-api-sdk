plugins {
    id("org.springframework.boot") version "3.2.4"
    kotlin("jvm") version "1.9.22"
    kotlin("kapt") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.22"
}

apply(plugin = "io.spring.dependency-management")

springBoot {
    buildInfo()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":api-sdk-all"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")

    testImplementation("org.wiremock:wiremock-standalone:3.3.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.bouncycastle:bcprov-jdk18on:1.78")
    testImplementation("org.bouncycastle:bcpkix-jdk18on:1.78")
}