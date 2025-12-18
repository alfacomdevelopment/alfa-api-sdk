package com.alfa.api.sdk.sample.app.transactions

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class Transactions1cTest : ParentIntegrationTest() {
    @Test
    fun positive() {
        mockGetStatement1cEndpoint()

        testClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/transactions/1C")
                    .queryParam("account", "40702810323180001677")
                    .queryParam("date", "2023-01-30")
                    .queryParam("limit", "1")
                    .queryParam("offset", "0")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.pagination.offset").isEqualTo(0)
            .jsonPath("$.pagination.limit").isEqualTo(10)
            .jsonPath("$.pagination.totalCount").isEqualTo(2)
            .jsonPath("$.statement.id").isEqualTo("032b9048-f970-5840-6ff6-73baf1b4819f")
            .jsonPath("$.statement.formatVersion").isEqualTo("2.3.2")
            .jsonPath("$.statement.creationDate").isEqualTo("2024-01-25T15:08:31.294")
            .jsonPath("$.statement.sender.bic").isEqualTo("012525593")
            .jsonPath("$.statement.sender.name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.statement.recipient.id").isEqualTo("40702810801300006319")
            .jsonPath("$.statement.recipient.name").isEqualTo("Полное наименование Орг № 72862")
            .jsonPath("$.statement.recipient.inn").isEqualTo("0122030587")
            .jsonPath("$.statement.recipient.kpp").isEqualTo("012368152")
            .jsonPath("$.statement.data.statementType").isEqualTo("0")
            .jsonPath("$.statement.data.dateFrom").isEqualTo("2023-11-09T00:00:00.000")
            .jsonPath("$.statement.data.dateTo").isEqualTo("2023-11-09T23:59:59.999")
            .jsonPath("$.statement.data.account").isEqualTo("40702810801300006319")
            .jsonPath("$.statement.data.bank.bic").isEqualTo("012525593")
            .jsonPath("$.statement.data.bank.name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.statement.data.openingBalance").isEqualTo(999.90)
            .jsonPath("$.statement.data.totalDebits").isEqualTo(0.02)
            .jsonPath("$.statement.data.totalCredits").isEqualTo(0.00)
            .jsonPath("$.statement.data.closingBalance").isEqualTo(999.88)
            .jsonPath("$.statement.data.stamp.bic").isEqualTo("012525593")
            .jsonPath("$.statement.data.stamp.name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.id").isEqualTo("1231109MOCOWSLK 08013")
            .jsonPath("$.statement.data.operationInfo[0].date").isEqualTo("2023-11-09")
            .jsonPath("$.statement.data.operationInfo[0].dc").isEqualTo("1")
            .jsonPath("$.statement.data.operationInfo[0].stamp.bic").isEqualTo("012525593")
            .jsonPath("$.statement.data.operationInfo[0].stamp.name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.statement.data.operationInfo[0].stamp.status.code").isEqualTo("02")
            .jsonPath("$.statement.data.operationInfo[0].stamp.status.name").isEqualTo("Исполнен")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.docNo").isEqualTo("8014")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.docDate").isEqualTo("2023-11-09")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.sum").isEqualTo(0.01)
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.name").isEqualTo("Полное наименование Орг № 72862")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.inn").isEqualTo("0122030587")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.kpp").isEqualTo("012368152")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.account").isEqualTo("40702810801300006319")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.bank.bic").isEqualTo("012525593")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.bank.name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.name").isEqualTo("Полное наименование Орг № 72862")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.inn").isEqualTo("0122030587")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.kpp").isEqualTo("012368152")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.account").isEqualTo("40702810901300006316")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.bank.bic").isEqualTo("012525593")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.bank.name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.paymentKind").isEqualTo("электронно")
            .jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.transitionKind").isEqualTo("09")
    }

    @Test
    fun negativeError404() {
        testClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/transactions/1C")
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
        mockGetEndpointWithInternalError("/api/accounts/(.+)/transactions/1C(.+)")

        testClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/transactions/1C")
                    .queryParam("account", "40702810323180001677")
                    .queryParam("date", "2023-01-30")
                    .queryParam("limit", "1")
                    .queryParam("offset", "0")
                    .build()
            }
            .exchange()
            .expectStatus().is5xxServerError
    }

    private fun mockGetStatement1cEndpoint() {
        wiremock.stubFor(
            WireMock.get(WireMock.urlMatching("/api/accounts/40702810323180001677/transactions/1C(.+)"))
                .withQueryParam("executeDate", WireMock.equalTo("2023-01-30"))
                .withQueryParam("limit", WireMock.equalTo("1"))
                .withQueryParam("offset", WireMock.equalTo("0"))
                .willReturn(
                    WireMock.aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                        .withBody(getMockBodyFromResources("mocks/transactions/statement-1c.xml"))
                )
        )
    }
}
