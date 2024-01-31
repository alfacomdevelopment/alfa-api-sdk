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

class TransactionsSummaryTest : ParentIntegrationTest() {
    @Test
    fun positive() {
        mockGetStatementSummaryEndpoint()
        //when
        val result = mockMvc.perform(
            get("/sdk/transactions/summary")
                .param("account", "40702810323180001677")
                .param("date", "2023-01-30")
        )

        // then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.composedDateTime").value("2018-12-31"))
            .andExpect(jsonPath("$.lastMovementDate").value("2018-12-31"))
            .andExpect(jsonPath("$.openingBalance.amount").value(10000.55))
            .andExpect(jsonPath("$.openingBalance.currencyName").value("RUR"))
            .andExpect(jsonPath("$.openingBalanceRub.amount").value(10000.55))
            .andExpect(jsonPath("$.openingBalanceRub.currencyName").value("RUR"))
            .andExpect(jsonPath("$.closingBalance.amount").value(25000.3))
            .andExpect(jsonPath("$.closingBalance.currencyName").value("RUR"))
            .andExpect(jsonPath("$.closingBalanceRub.amount").value(25000.3))
            .andExpect(jsonPath("$.closingBalanceRub.currencyName").value("RUR"))
            .andExpect(jsonPath("$.debitTurnover.amount").value(10000))
            .andExpect(jsonPath("$.debitTurnover.currencyName").value("RUR"))
            .andExpect(jsonPath("$.debitTransactionsNumber").value(10))
            .andExpect(jsonPath("$.debitTurnoverRub.amount").value(10000))
            .andExpect(jsonPath("$.debitTurnoverRub.currencyName").value("RUR"))
            .andExpect(jsonPath("$.creditTurnover.amount").value(24999.75))
            .andExpect(jsonPath("$.creditTurnover.currencyName").value("RUR"))
            .andExpect(jsonPath("$.creditTransactionsNumber").value(10))
            .andExpect(jsonPath("$.creditTurnoverRub.amount").value(24999.75))
            .andExpect(jsonPath("$.creditTurnoverRub.currencyName").value("RUR"))
            .andReturn()
    }

    @Test
    fun negativeError404() {
        //when
        val result = mockMvc.perform(
            get("/sdk/transactions/summary")
                .param("account", "40702810323180001677")
                .param("date", "2023-01-30")
        )

        // then
        result.andExpect {
            assertTrue(it.resolvedException is SdkException)
            assertTrue(it.resolvedException!!.cause is ApiException)
            val exception = it.resolvedException as SdkException
            val cause = it.resolvedException!!.cause as ApiException
            assertEquals("Error occurred while receiving account summary", exception.message)
            assertEquals(404, cause.statusCode)
        }.andReturn()
    }

    @Test
    fun negativeError500() {
        mockGetEndpointWithInternalError("/api/statement/summary(.+)")

        //when
        val result = mockMvc.perform(
            get("/sdk/transactions/summary")
                .param("account", "40702810323180001677")
                .param("date", "2023-01-30")
        )

        // then
        result.andExpect {
            assertTrue(it.resolvedException is SdkException)
            assertTrue(it.resolvedException!!.cause is ApiException)
            val exception = it.resolvedException as SdkException
            val cause = it.resolvedException!!.cause as ApiException
            assertEquals("Error occurred while receiving account summary", exception.message)
            assertEquals(500, cause.statusCode)
            assertEquals("{\"error\": \"Internal Server Error\"}", String(cause.response, StandardCharsets.UTF_8))
        }.andReturn()
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