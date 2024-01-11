package com.alfa.api.sdk.sample.app

import org.junit.jupiter.api.BeforeEach
import org.springframework.core.io.ClassPathResource
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets

class ParentIntegrationTest : AbstractIntegrationTest() {
    @BeforeEach
    fun beforeEach() {
        wiremock.resetMappings()
    }

    protected fun getMockBodyFromResources(path: String): String = StreamUtils.copyToString(
        ClassPathResource(path).inputStream,
        StandardCharsets.UTF_8
    )
}