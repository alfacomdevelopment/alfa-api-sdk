package com.alfa.api.sdk.sample.app.crypto

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

class CmsSignatureTest : ParentIntegrationTest() {
    @Test
    fun positiveAttached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-mt940.txt")

        val signedContent = testClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/crypto/sign/rsa/cms")
                    .queryParam("attached", "true")
                    .build()
            }
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(data)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody ?: error("Empty response")

        testClient.post()
            .uri("/sdk/crypto/verify/rsa/attached/cms")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(signedContent)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("true")
    }

    @Test
    fun positiveDetached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-mt940.txt")

        val signature = testClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/crypto/sign/rsa/cms")
                    .queryParam("attached", "false")
                    .build()
            }
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(data)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody ?: error("Empty response")

        testClient.post()
            .uri("/sdk/crypto/verify/rsa/detached/cms")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header("signature", signature)
            .body(data)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("true")
    }

    @Test
    fun negativeDetached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-mt940.txt")

        val signature = testClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/crypto/sign/rsa/cms")
                    .queryParam("attached", "false")
                    .build()
            }
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(data)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody ?: error("Empty response")

        testClient.post()
            .uri("/sdk/crypto/verify/rsa/detached/cms")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header("signature", signature)
            .body("changed content")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("false")
    }
}
