package com.alfa.api.sdk.sample.app.crypto

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CmsSignatureTest : ParentIntegrationTest() {
    @Test
    fun positiveAttached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-mt940.txt")

        val signedContent = mockMvc.perform(
            post("/sdk/crypto/sign/rsa/cms")
                .param("attached", "true")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(data)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        mockMvc.perform(
            post("/sdk/crypto/verify/rsa/attached/cms")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(signedContent)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    fun positiveDetached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-mt940.txt")

        val signature = mockMvc.perform(
            post("/sdk/crypto/sign/rsa/cms")
                .param("attached", "false")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(data)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        mockMvc.perform(
            post("/sdk/crypto/verify/rsa/detached/cms")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("signature", signature)
                .content(data)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    fun negativeDetached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-mt940.txt")

        val signature = mockMvc.perform(
            post("/sdk/crypto/sign/rsa/cms")
                .param("attached", "false")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(data)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        mockMvc.perform(
            post("/sdk/crypto/verify/rsa/detached/cms")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("signature", signature)
                .content("changed content")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("false"))
    }
}