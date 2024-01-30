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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.StandardCharsets

class TransactionsMt940Test : ParentIntegrationTest() {
    @Test
    fun positive() {
        mockGetStatementMt940Endpoint()
        val mt940response = getMockBodyFromResources("mocks/transactions/statement-mt940.txt")

        //when
        val result = mockMvc.perform(
            get("/sdk/transactions/MT940")
                .param("account", "40702810323180001677")
                .param("date", "2023-01-30")
                .param("limit", "1")
                .param("offset", "0")
        )

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string(mt940response))
            .andReturn()
    }

    @Test
    fun negativeError404() {
        //when
        val result = mockMvc.perform(
            get("/sdk/transactions/MT940")
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
            assertEquals("Error occurred while receiving statement in MT940 format", exception.message)
            assertEquals(404, cause.statusCode)
        }.andReturn()
    }

    @Test
    fun negativeError500() {
        mockGetEndpointWithInternalError("/api/accounts/(.+)/transactions/MT940(.+)")

        //when
        val result = mockMvc.perform(
            get("/sdk/transactions/MT940")
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
            assertEquals("Error occurred while receiving statement in MT940 format", exception.message)
            assertEquals(500, cause.statusCode)
            assertEquals("{\"error\": \"Internal Server Error\"}", String(cause.response, StandardCharsets.UTF_8))
        }.andReturn()
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