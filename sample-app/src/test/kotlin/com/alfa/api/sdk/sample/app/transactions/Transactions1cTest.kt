package com.alfa.api.sdk.sample.app.transactions

import com.alfa.api.sdk.client.exceptions.ApiException
import com.alfa.api.sdk.common.exceptions.SdkException
import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.StandardCharsets

class Transactions1cTest : ParentIntegrationTest() {
    @Test
    fun positive() {
        mockGetStatement1cEndpoint()
        //when
        val result = mockMvc.perform(
            get("/sdk/transactions/1C")
                .param("account", "40702810323180001677")
                .param("date", "2023-01-30")
                .param("limit", "1")
                .param("offset", "0")
        )

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.pagination.offset").value(0))
            .andExpect(jsonPath("$.pagination.limit").value(10))
            .andExpect(jsonPath("$.pagination.totalCount").value(2))
            .andExpect(jsonPath("$.statement.id").value("032b9048-f970-5840-6ff6-73baf1b4819f"))
            .andExpect(jsonPath("$.statement.formatVersion").value("2.3.2"))
            .andExpect(jsonPath("$.statement.creationDate").value("2024-01-25T15:08:31.294"))
            .andExpect(jsonPath("$.statement.sender.bic").value("012525593"))
            .andExpect(jsonPath("$.statement.sender.name").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.statement.recipient.id").value("40702810801300006319"))
            .andExpect(jsonPath("$.statement.recipient.name").value("Полное наименование Орг № 72862"))
            .andExpect(jsonPath("$.statement.recipient.inn").value("0122030587"))
            .andExpect(jsonPath("$.statement.recipient.kpp").value("012368152"))
            .andExpect(jsonPath("$.statement.data.statementType").value("0"))
            .andExpect(jsonPath("$.statement.data.dateFrom").value("2023-11-09T00:00:00.000"))
            .andExpect(jsonPath("$.statement.data.dateTo").value("2023-11-09T23:59:59.999"))
            .andExpect(jsonPath("$.statement.data.account").value("40702810801300006319"))
            .andExpect(jsonPath("$.statement.data.bank.bic").value("012525593"))
            .andExpect(jsonPath("$.statement.data.bank.name").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.statement.data.openingBalance").value(999.90))
            .andExpect(jsonPath("$.statement.data.totalDebits").value(0.02))
            .andExpect(jsonPath("$.statement.data.totalCredits").value(0.00))
            .andExpect(jsonPath("$.statement.data.closingBalance").value(999.88))
            .andExpect(jsonPath("$.statement.data.stamp.bic").value("012525593"))
            .andExpect(jsonPath("$.statement.data.stamp.name").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.id").value("1231109MOCOWSLK 08013"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].date").value("2023-11-09"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].dc").value("1"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].stamp.bic").value("012525593"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].stamp.name").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].stamp.status.code").value("02"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].stamp.status.name").value("Исполнен"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.docNo").value("8014"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.docDate").value("2023-11-09"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.sum").value(0.01))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.name").value("Полное наименование Орг № 72862"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.inn").value("0122030587"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.kpp").value("012368152"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.account").value("40702810801300006319"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.bank.bic").value("012525593"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payer.bank.name").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.name").value("Полное наименование Орг № 72862"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.inn").value("0122030587"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.kpp").value("012368152"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.account").value("40702810901300006316"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.bank.bic").value("012525593"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.payee.bank.name").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.paymentKind").value("электронно"))
            .andExpect(jsonPath("$.statement.data.operationInfo[0].payDoc.memOrder.transitionKind").value("09"))
            .andReturn()
    }

    @Test
    fun negativeError404() {
        //when
        val result = mockMvc.perform(
            get("/sdk/transactions/1C")
                .param("account", "40702810323180001677")
                .param("date", "2023-01-30")
                .param("limit", "1")
                .param("offset", "0")
        )

        // then
        result.andExpect {
            assertTrue(it.resolvedException is SdkException)
            assertTrue(it.resolvedException!!.cause is ApiException)
            val exception = it.resolvedException as SdkException
            val cause = it.resolvedException!!.cause as ApiException
            assertEquals("Error occurred while receiving statement in 1C format", exception.message)
            assertEquals(404, cause.statusCode)
        }.andReturn()
    }

    @Test
    fun negativeError500() {
        mockGetEndpointWithInternalError("/api/accounts/(.+)/transactions/1C(.+)")

        //when
        val result = mockMvc.perform(
            get("/sdk/transactions/1C")
                .param("account", "40702810323180001677")
                .param("date", "2023-01-30")
                .param("limit", "1")
                .param("offset", "0")
        )

        // then
        result.andExpect {
            assertTrue(it.resolvedException is SdkException)
            assertTrue(it.resolvedException!!.cause is ApiException)
            val exception = it.resolvedException as SdkException
            val cause = it.resolvedException!!.cause as ApiException
            assertEquals("Error occurred while receiving statement in 1C format", exception.message)
            assertEquals(500, cause.statusCode)
            assertEquals("{\"error\": \"Internal Server Error\"}", String(cause.response, StandardCharsets.UTF_8))
        }.andReturn()
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