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
            .jsonPath("$.Pagination.Offset").isEqualTo(0)
            .jsonPath("$.Pagination.Limit").isEqualTo(10)
            .jsonPath("$.Pagination.TotalCount").isEqualTo(2)
            .jsonPath("$.Statement.id").isEqualTo("032b9048-f970-5840-6ff6-73baf1b4819f")
            .jsonPath("$.Statement.formatVersion").isEqualTo("2.3.2")
            .jsonPath("$.Statement.creationDate").isEqualTo("2024-01-25T15:08:31.294")
            .jsonPath("$.Statement.Sender.bic").isEqualTo("012525593")
            .jsonPath("$.Statement.Sender.name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.Statement.Recipient.id").isEqualTo("40702810801300006319")
            .jsonPath("$.Statement.Recipient.name").isEqualTo("Полное наименование Орг № 72862")
            .jsonPath("$.Statement.Recipient.inn").isEqualTo("0122030587")
            .jsonPath("$.Statement.Recipient.kpp").isEqualTo("012368152")
            .jsonPath("$.Statement.Data.StatementType").isEqualTo("0")
            .jsonPath("$.Statement.Data.DateFrom").isEqualTo("2023-11-09T00:00:00.000")
            .jsonPath("$.Statement.Data.DateTo").isEqualTo("2023-11-09T23:59:59.999")
            .jsonPath("$.Statement.Data.Account").isEqualTo("40702810801300006319")
            .jsonPath("$.Statement.Data.Bank.Bic").isEqualTo("012525593")
            .jsonPath("$.Statement.Data.Bank.Name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.Statement.Data.OpeningBalance").isEqualTo(999.90)
            .jsonPath("$.Statement.Data.TotalDebits").isEqualTo(0.02)
            .jsonPath("$.Statement.Data.TotalCredits").isEqualTo(0.00)
            .jsonPath("$.Statement.Data.ClosingBalance").isEqualTo(999.88)
            .jsonPath("$.Statement.Data.Stamp.Bic").isEqualTo("012525593")
            .jsonPath("$.Statement.Data.Stamp.Name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.id").isEqualTo("1231109MOCOWSLK 08013")
            .jsonPath("$.Statement.Data.OperationInfo[0].Date").isEqualTo("2023-11-09")
            .jsonPath("$.Statement.Data.OperationInfo[0].Dc").isEqualTo("1")
            .jsonPath("$.Statement.Data.OperationInfo[0].Stamp.Bic").isEqualTo("012525593")
            .jsonPath("$.Statement.Data.OperationInfo[0].Stamp.Name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.Statement.Data.OperationInfo[0].Stamp.Status.Code").isEqualTo("02")
            .jsonPath("$.Statement.Data.OperationInfo[0].Stamp.Status.Name").isEqualTo("Исполнен")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.DocNo").isEqualTo("8014")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.DocDate").isEqualTo("2023-11-09")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Sum").isEqualTo(0.01)
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payer.Name").isEqualTo("Полное наименование Орг № 72862")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payer.Inn").isEqualTo("0122030587")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payer.Kpp").isEqualTo("012368152")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payer.Account").isEqualTo("40702810801300006319")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payer.Bank.Bic").isEqualTo("012525593")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payer.Bank.Name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payee.Name").isEqualTo("Полное наименование Орг № 72862")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payee.Inn").isEqualTo("0122030587")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payee.Kpp").isEqualTo("012368152")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payee.Account").isEqualTo("40702810901300006316")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payee.Bank.Bic").isEqualTo("012525593")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.Payee.Bank.Name").isEqualTo("АО \"ТЕСТ-БАНК\"")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.paymentKind").isEqualTo("электронно")
            .jsonPath("$.Statement.Data.OperationInfo[0].PayDoc.MemOrder.transitionKind").isEqualTo("09")
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
