package com.alfa.api.sdk.sample.app

import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.BeforeEach
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets

class ParentIntegrationTest : AbstractIntegrationTest() {
    @BeforeEach
    fun beforeEach() {
        wiremock.resetMappings()
    }

    protected fun mockGetEndpointWithInternalError(urlPattern: String) {
        wiremock.stubFor(
            WireMock.get(WireMock.urlMatching(urlPattern)).willReturn(
                WireMock.aResponse().withStatus(500)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(
                        """
                        {"error": "Internal Server Error"}
                    """.trimIndent()
                    )
            )
        )
    }

    protected fun getMockBodyFromResources(path: String): String = StreamUtils.copyToString(
        ClassPathResource(path).inputStream,
        StandardCharsets.UTF_8
    )
}