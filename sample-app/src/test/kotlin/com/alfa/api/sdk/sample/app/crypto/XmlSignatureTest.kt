package com.alfa.api.sdk.sample.app.crypto

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

class XmlSignatureTest : ParentIntegrationTest() {
    @Test
    fun positiveAttached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-1c.xml")

        val signedContent = testClient.post()
            .uri("/sdk/crypto/sign/rsa/xml")
            .contentType(MediaType.APPLICATION_XML)
            .body(data)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody ?: error("Empty response")

        testClient.post()
            .uri("/sdk/crypto/verify/rsa/xml")
            .contentType(MediaType.APPLICATION_XML)
            .body(signedContent)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("true")
    }

    @Test
    fun negativeAttached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-1c.xml")

        val signedContent = testClient.post()
            .uri("/sdk/crypto/sign/rsa/xml")
            .contentType(MediaType.APPLICATION_XML)
            .body(data)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody ?: error("Empty response")

        testClient.post()
            .uri("/sdk/crypto/verify/rsa/xml")
            .contentType(MediaType.APPLICATION_XML)
            .body(signedContent + "changed data")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("false")
    }
}
