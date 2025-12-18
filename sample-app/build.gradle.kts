plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.spring)
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
    implementation(libs.kotlin.reflect)
    implementation(libs.mapstruct)

    kapt(libs.mapstruct.processor)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.wiremock.standalone)
    testImplementation(libs.bcprov.jdk18on)
    testImplementation(libs.bcpkix.jdk18on)
}