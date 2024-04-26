package com.alfa.api.sdk.sample.app.crypto

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class XmlSignatureTest : ParentIntegrationTest() {
    @Test
    fun positiveAttached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-1c.xml")

        val signedContent = mockMvc.perform(
            post("/sdk/crypto/sign/rsa/xml")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(data)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        mockMvc.perform(
            post("/sdk/crypto/verify/rsa/xml")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(signedContent)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }

    @Test
    fun negativeAttached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-1c.xml")

        val signedContent = mockMvc.perform(
            post("/sdk/crypto/sign/rsa/xml")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(data)
        )
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        mockMvc.perform(
            post("/sdk/crypto/verify/rsa/xml")
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .content(signedContent + "changed data")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("false"))
    }
}