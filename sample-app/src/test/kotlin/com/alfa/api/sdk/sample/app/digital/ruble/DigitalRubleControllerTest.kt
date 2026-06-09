package com.alfa.api.sdk.sample.app.digital.ruble

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class DigitalRubleControllerTest : ParentIntegrationTest() {
    @Test
    fun getWalletContext() {
        stubGet("/api/jp/v1/digital-ruble/wallet-context", "mocks/digital-ruble/wallet-context.json")

        testClient.get()
            .uri("/sdk/digital-ruble/wallet-context")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.participantId").isEqualTo("participant-1")
            .jsonPath("$.participantWalletId").isEqualTo("wallet-1")
            .jsonPath("$.devices[0].deviceId").isEqualTo("device-1")
            .jsonPath("$.devices[0].certificateSerialNumber").isEqualTo("serial-1")

        verifyGatewayHeadersAreAbsent(WireMock.getRequestedFor(WireMock.urlEqualTo("/api/jp/v1/digital-ruble/wallet-context")))
    }

    @Test
    fun getCertificates() {
        stubGet("/api/jp/v1/digital-ruble/certificates", "mocks/digital-ruble/certificates.json")

        testClient.get()
            .uri("/sdk/digital-ruble/certificates")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.rcdOfBankOfRussia[0]").isEqualTo("rcd-bank-of-russia-1")
            .jsonPath("$.certificateBankProcessing[0]").isEqualTo("certificate-bank-processing-1")

        verifyGatewayHeadersAreAbsent(WireMock.getRequestedFor(WireMock.urlEqualTo("/api/jp/v1/digital-ruble/certificates")))
    }

    @Test
    fun getStatementTransactions() {
        val apiPath = "/api/jp/v1/digital-ruble/statement/transactions"
        wiremock.stubFor(
            WireMock.post(WireMock.urlEqualTo(apiPath))
                .withRequestBody(WireMock.equalToJson(statementRequest))
                .willReturn(
                    WireMock.aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(getMockBodyFromResources("mocks/digital-ruble/statement-response.json"))
                )
        )

        testClient.post()
            .uri("/sdk/digital-ruble/statement/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .body(statementRequest)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.pagination.limit").isEqualTo(10)
            .jsonPath("$.pagination.totalCount").isEqualTo(1)
            .jsonPath("$.statement.historyFilters.periodFrom").isEqualTo("2026-05-01")
            .jsonPath("$.statement.transactions[0].transactionDate").isEqualTo("2026-05-02T10:15:30Z")
            .jsonPath("$.statement.transactions[0].amount").isEqualTo("5.00")

        verifyGatewayHeadersAreAbsent(WireMock.postRequestedFor(WireMock.urlEqualTo(apiPath)))
    }

    private fun stubGet(path: String, responseResource: String) {
        wiremock.stubFor(
            WireMock.get(WireMock.urlEqualTo(path))
                .willReturn(
                    WireMock.aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(getMockBodyFromResources(responseResource))
                )
        )
    }

    private fun verifyGatewayHeadersAreAbsent(requestPattern: RequestPatternBuilder) {
        wiremock.verify(
            requestPattern
                .withHeader("X-ClientId", WireMock.absent())
                .withHeader("X-Cus", WireMock.absent())
                .withHeader("X-Acus", WireMock.absent())
                .withHeader("X-Sub", WireMock.absent())
        )
    }

    companion object {
        private val statementRequest = """
            {
              "deviceId": "device-1",
              "fingerprint": "fingerprint-1",
              "participantId": "participant-1",
              "participantWalletId": "wallet-1",
              "cbdcMessage": "message",
              "historyFilters": {
                "periodFrom": "2026-05-01",
                "periodTo": "2026-05-10",
                "transactionType": "BUY",
                "direction": "INCOMING"
              },
              "pagination": {
                "limit": 10,
                "offset": 0
              }
            }
        """.trimIndent()
    }
}
