package com.alfa.api.sdk.sample.app.customer.info

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource

@TestPropertySource(
    properties = [
        "sdk-sample-app.auth-type=API_KEY",
        "sdk-sample-app.api-key=sample-api-key"
    ]
)
class CustomerInfoControllerApiKeyTest : ParentIntegrationTest() {
    @Test
    fun shouldSendApiKeyAuthorizationHeader() {
        wiremock.stubFor(
            WireMock.get(WireMock.urlEqualTo("/api/jp/v2/customer-info"))
                .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("ApiKey sample-api-key"))
                .willReturn(
                    WireMock.aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(getMockBodyFromResources("mocks/customer-info/customer-info-v2.json"))
                )
        )

        testClient.get()
            .uri("/sdk/customer-info")
            .exchange()
            .expectStatus().isOk

        wiremock.verify(
            WireMock.getRequestedFor(WireMock.urlEqualTo("/api/jp/v2/customer-info"))
                .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("ApiKey sample-api-key"))
        )
    }
}
