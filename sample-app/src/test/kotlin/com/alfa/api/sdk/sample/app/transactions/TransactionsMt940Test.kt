package com.alfa.api.sdk.sample.app.transactions

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class TransactionsMt940Test : ParentIntegrationTest() {
    @Test
    fun positive() {
        mockGetStatementMt940Endpoint()
        val mt940response = getMockBodyFromResources("mocks/transactions/statement-mt940.txt")

        testClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/transactions/MT940")
                    .queryParam("account", "40702810323180001677")
                    .queryParam("date", "2023-01-30")
                    .queryParam("limit", "1")
                    .queryParam("offset", "0")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo(mt940response)
    }

    @Test
    fun negativeError404() {
        testClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/transactions/MT940")
                    .queryParam("account", "40702810323180001677")
                    .queryParam("date", "2023-01-30")
                    .queryParam("limit", "1")
                    .queryParam("offset", "0")
                    .build()
            }
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun negativeError500() {
        mockGetEndpointWithInternalError("/api/accounts/(.+)/transactions/MT940(.+)")

        testClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/transactions/MT940")
                    .queryParam("account", "40702810323180001677")
                    .queryParam("date", "2023-01-30")
                    .queryParam("limit", "1")
                    .queryParam("offset", "0")
                    .build()
            }
            .exchange()
            .expectStatus().is5xxServerError
    }

    private fun mockGetStatementMt940Endpoint() {
        wiremock.stubFor(
            WireMock.get(WireMock.urlMatching("/api/accounts/40702810323180001677/transactions/MT940(.+)"))
                .withQueryParam("executeDate", WireMock.equalTo("2023-01-30"))
                .withQueryParam("limit", WireMock.equalTo("1"))
                .withQueryParam("offset", WireMock.equalTo("0"))
                .willReturn(
                    WireMock.aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                        .withBody(getMockBodyFromResources("mocks/transactions/statement-mt940.txt"))
                )
        )
    }
}
