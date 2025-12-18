package com.alfa.api.sdk.sample.app.crypto

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

class JwsSignatureTest : ParentIntegrationTest() {
    @Test
    fun positiveAttached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-summary.json")

        val signedContent = testClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/crypto/sign/rsa/jws")
                    .queryParam("attached", "true")
                    .build()
            }
            .contentType(MediaType.APPLICATION_JSON)
            .body(data)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody ?: error("Empty response")

        testClient.post()
            .uri("/sdk/crypto/verify/rsa/attached/jws")
            .contentType(MediaType.valueOf("application/jose"))
            .body(signedContent)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("true")
    }

    @Test
    fun positiveDetached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-summary.json")

        val signature = testClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/crypto/sign/rsa/jws")
                    .queryParam("attached", "false")
                    .build()
            }
            .contentType(MediaType.APPLICATION_JSON)
            .body(data)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody ?: error("Empty response")

        testClient.post()
            .uri("/sdk/crypto/verify/rsa/detached/jws")
            .contentType(MediaType.APPLICATION_JSON)
            .header("jwsWithoutData", signature)
            .body(data)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("true")
    }

    @Test
    fun negativeDetached() {
        val data = getMockBodyFromResources("mocks/transactions/statement-summary.json")

        val signature = testClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/sdk/crypto/sign/rsa/jws")
                    .queryParam("attached", "false")
                    .build()
            }
            .contentType(MediaType.APPLICATION_JSON)
            .body(data)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()
            .responseBody ?: error("Empty response")

        testClient.post()
            .uri("/sdk/crypto/verify/rsa/detached/jws")
            .contentType(MediaType.APPLICATION_JSON)
            .header("jwsWithoutData", signature)
            .body("changed content")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("false")
    }
}
