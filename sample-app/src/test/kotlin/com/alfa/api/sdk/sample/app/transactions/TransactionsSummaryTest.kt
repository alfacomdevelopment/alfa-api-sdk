package com.alfa.api.sdk.sample.app.transactions

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class TransactionsSummaryTest : ParentIntegrationTest() {
    @Test
    fun positive() {
        mockGetStatementSummaryEndpoint()

        testClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/transactions/summary")
                    .queryParam("account", "40702810323180001677")
                    .queryParam("date", "2023-01-30")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.composedDateTime").isEqualTo("2018-12-31")
            .jsonPath("$.lastMovementDate").isEqualTo("2018-12-31")
            .jsonPath("$.openingBalance.amount").isEqualTo(10000.55)
            .jsonPath("$.openingBalance.currencyName").isEqualTo("RUR")
            .jsonPath("$.openingBalanceRub.amount").isEqualTo(10000.55)
            .jsonPath("$.openingBalanceRub.currencyName").isEqualTo("RUR")
            .jsonPath("$.closingBalance.amount").isEqualTo(25000.3)
            .jsonPath("$.closingBalance.currencyName").isEqualTo("RUR")
            .jsonPath("$.closingBalanceRub.amount").isEqualTo(25000.3)
            .jsonPath("$.closingBalanceRub.currencyName").isEqualTo("RUR")
            .jsonPath("$.debitTurnover.amount").isEqualTo(10000)
            .jsonPath("$.debitTurnover.currencyName").isEqualTo("RUR")
            .jsonPath("$.debitTransactionsNumber").isEqualTo(10)
            .jsonPath("$.debitTurnoverRub.amount").isEqualTo(10000)
            .jsonPath("$.debitTurnoverRub.currencyName").isEqualTo("RUR")
            .jsonPath("$.creditTurnover.amount").isEqualTo(24999.75)
            .jsonPath("$.creditTurnover.currencyName").isEqualTo("RUR")
            .jsonPath("$.creditTransactionsNumber").isEqualTo(10)
            .jsonPath("$.creditTurnoverRub.amount").isEqualTo(24999.75)
            .jsonPath("$.creditTurnoverRub.currencyName").isEqualTo("RUR")
    }

    @Test
    fun negativeError404() {
        testClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/transactions/summary")
                    .queryParam("account", "40702810323180001677")
                    .queryParam("date", "2023-01-30")
                    .build()
            }
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun negativeError500() {
        mockGetEndpointWithInternalError("/api/statement/summary(.+)")

        testClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/transactions/summary")
                    .queryParam("account", "40702810323180001677")
                    .queryParam("date", "2023-01-30")
                    .build()
            }
            .exchange()
            .expectStatus().is5xxServerError
    }

    private fun mockGetStatementSummaryEndpoint() {
        wiremock.stubFor(
            WireMock.get(WireMock.urlMatching("/api/statement/summary(.+)"))
                .withQueryParam("accountNumber", WireMock.equalTo("40702810323180001677"))
                .withQueryParam("statementDate", WireMock.equalTo("2023-01-30"))
                .willReturn(
                    WireMock.aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(getMockBodyFromResources("mocks/transactions/statement-summary.json"))
                )
        )
    }
}
