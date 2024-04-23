package com.alfa.api.sdk.sample.app.crypto

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class JwsSignatureTest : ParentIntegrationTest() {
    @Test
    fun positiveAttached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-summary.json")

        val signedContent = mockMvc.perform(
            post("/sdk/crypto/sign/rsa/jws")
                .param("attached", "true")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(data)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        mockMvc.perform(
            post("/sdk/crypto/verify/rsa/attached/jws")
                .contentType("application/jose")
                .content(signedContent)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    fun positiveDetached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-summary.json")

        val signature = mockMvc.perform(
            post("/sdk/crypto/sign/rsa/jws")
                .param("attached", "false")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(data)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        mockMvc.perform(
            post("/sdk/crypto/verify/rsa/detached/jws")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("jwsWithoutData", signature)
                .content(data)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    fun negativeDetached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-summary.json")

        val signature = mockMvc.perform(
            post("/sdk/crypto/sign/rsa/jws")
                .param("attached", "false")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(data)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        mockMvc.perform(
            post("/sdk/crypto/verify/rsa/detached/jws")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("jwsWithoutData", signature)
                .content("changed content")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("false"))
    }
}