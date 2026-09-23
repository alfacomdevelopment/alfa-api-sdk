package com.alfa.api.sdk.sample.app.signature

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class SignatureControllerTest : ParentIntegrationTest() {
    @Test
    fun getDn() {
        stubJson(WireMock.get(WireMock.urlEqualTo("/api/jp/v3/signature/dn")), """{"dn":"dn-value"}""")

        testClient.get()
            .uri("/sdk/signature/dn")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.dn").isEqualTo("dn-value")
    }

    @Test
    fun getDnFile() {
        stubText(WireMock.get(WireMock.urlEqualTo("/api/jp/v3/signature/dn/file")), "dn-file")

        testClient.get()
            .uri("/sdk/signature/dn/file")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("dn-file")
    }

    @Test
    fun issueRsaCertificate() {
        stubJson(WireMock.post(WireMock.urlEqualTo("/api/jp/v3/signature/rsa-certificates/requests/issue")))

        testClient.post()
            .uri("/sdk/signature/rsa-certificates/requests/issue")
            .contentType(MediaType.APPLICATION_JSON)
            .body(issueRequest)
            .exchange()
            .expectStatus().isOk
        verifyJson(WireMock.postRequestedFor(WireMock.urlEqualTo("/api/jp/v3/signature/rsa-certificates/requests/issue")), issueRequest)
    }

    @Test
    fun sendRequest() {
        stubJson(WireMock.post(WireMock.urlEqualTo("/api/jp/v3/signature/requests/request-1/operations")))

        testClient.post()
            .uri("/sdk/signature/requests/request-1/operations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(postOperationsRequest)
            .exchange()
            .expectStatus().isOk
        verifyJson(WireMock.postRequestedFor(WireMock.urlEqualTo("/api/jp/v3/signature/requests/request-1/operations")), postOperationsRequest)
    }

    @Test
    fun signRequest() {
        stubJson(WireMock.put(WireMock.urlEqualTo("/api/jp/v3/signature/requests/operations/operation-1")))

        testClient.put()
            .uri("/sdk/signature/requests/operations/operation-1")
            .contentType(MediaType.APPLICATION_JSON)
            .body(putOperationsRequest)
            .exchange()
            .expectStatus().isOk
        verifyJson(WireMock.putRequestedFor(WireMock.urlEqualTo("/api/jp/v3/signature/requests/operations/operation-1")), putOperationsRequest)
    }

    @Test
    fun getRequest() {
        stubJson(WireMock.get(WireMock.urlEqualTo("/api/jp/v3/signature/requests/request-1")))

        testClient.get()
            .uri("/sdk/signature/requests/request-1")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun getRsaCertificate() {
        stubJson(WireMock.get(WireMock.urlEqualTo("/api/jp/v3/signature/rsa-certificates/certificate-1")))

        testClient.get()
            .uri("/sdk/signature/rsa-certificates/certificate-1")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun getRsaCertificates() {
        stubJson(WireMock.get(WireMock.urlEqualTo("/api/jp/v3/signature/rsa-certificates")))

        testClient.get()
            .uri("/sdk/signature/rsa-certificates")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun activateRsaCertificate() {
        stubJson(WireMock.post(WireMock.urlEqualTo("/api/jp/v3/signature/rsa-certificates/certificate-1/requests/activation")))

        testClient.post()
            .uri("/sdk/signature/rsa-certificates/certificate-1/requests/activation")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun changeSignMethod() {
        stubJson(WireMock.put(WireMock.urlEqualTo("/api/jp/v3/signature/sign-settings")))

        testClient.put()
            .uri("/sdk/signature/sign-settings")
            .contentType(MediaType.APPLICATION_JSON)
            .body(signSettingsRequest)
            .exchange()
            .expectStatus().isOk
        verifyJson(WireMock.putRequestedFor(WireMock.urlEqualTo("/api/jp/v3/signature/sign-settings")), signSettingsRequest)
    }

    @Test
    fun switchRsaCertificate() {
        stubJson(WireMock.post(WireMock.urlEqualTo("/api/jp/v3/signature/rsa-certificates/certificate-1/requests/switch")))

        testClient.post()
            .uri("/sdk/signature/rsa-certificates/certificate-1/requests/switch")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun reissueRsaCertificate() {
        stubJson(WireMock.post(WireMock.urlEqualTo("/api/jp/v3/signature/rsa-certificates/certificate-1/requests/reissue")))

        testClient.post()
            .uri("/sdk/signature/rsa-certificates/certificate-1/requests/reissue")
            .contentType(MediaType.APPLICATION_JSON)
            .body(reissueRequest)
            .exchange()
            .expectStatus().isOk
        verifyJson(WireMock.postRequestedFor(WireMock.urlEqualTo("/api/jp/v3/signature/rsa-certificates/certificate-1/requests/reissue")), reissueRequest)
    }

    @Test
    fun revokeRsaCertificate() {
        stubJson(WireMock.post(WireMock.urlEqualTo("/api/jp/v3/signature/rsa-certificates/certificate-1/requests/revocation")))

        testClient.post()
            .uri("/sdk/signature/rsa-certificates/certificate-1/requests/revocation")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun createQesRequest() {
        stubJson(WireMock.post(WireMock.urlEqualTo("/api/jp/v3/signature/qes/requests")))

        testClient.post()
            .uri("/sdk/signature/qes/requests")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun sendQesRequest() {
        stubJson(WireMock.post(WireMock.urlEqualTo("/api/jp/v3/signature/qes/requests/qes-1")))

        testClient.post()
            .uri("/sdk/signature/qes/requests/qes-1")
            .contentType(MediaType.APPLICATION_JSON)
            .body(qesSendRequest)
            .exchange()
            .expectStatus().isOk
        verifyJson(WireMock.postRequestedFor(WireMock.urlEqualTo("/api/jp/v3/signature/qes/requests/qes-1")), qesSendRequest)
    }

    @Test
    fun signQesRequest() {
        stubJson(WireMock.patch(WireMock.urlEqualTo("/api/jp/v3/signature/qes/requests/qes-1")))

        testClient.patch()
            .uri("/sdk/signature/qes/requests/qes-1")
            .contentType(MediaType.APPLICATION_JSON)
            .body(qesSignRequest)
            .exchange()
            .expectStatus().isOk
        verifyJson(WireMock.patchRequestedFor(WireMock.urlEqualTo("/api/jp/v3/signature/qes/requests/qes-1")), qesSignRequest)
    }

    @Test
    fun registerPowerOfAttorney() {
        stubJson(WireMock.post(WireMock.urlEqualTo("/api/jp/v3/signature/powers-of-attorney/requests")), """{"id":"poa-1"}""")

        testClient.post()
            .uri("/sdk/signature/powers-of-attorney/requests")
            .contentType(MediaType.APPLICATION_JSON)
            .body(poaRequest)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo("poa-1")
        verifyJson(WireMock.postRequestedFor(WireMock.urlEqualTo("/api/jp/v3/signature/powers-of-attorney/requests")), poaRequest)
    }

    @Test
    fun getPowerOfAttorneyStatus() {
        stubJson(
            WireMock.get(WireMock.urlEqualTo("/api/jp/v3/signature/powers-of-attorney/requests/poa-1/status")),
            """{"status":"COMPLETE"}"""
        )

        testClient.get()
            .uri("/sdk/signature/powers-of-attorney/requests/poa-1/status")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.status").isEqualTo("COMPLETE")
    }

    @Test
    fun downloadPowerOfAttorney() {
        stubBinary(WireMock.get(WireMock.urlEqualTo("/api/jp/v3/signature/powers-of-attorney/requests/poa-1")), "file-content")

        testClient.get()
            .uri("/sdk/signature/powers-of-attorney/requests/poa-1")
            .header(HttpHeaders.ACCEPT, MediaType.valueOf("application/zip").toString())
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.valueOf("application/zip"))
    }

    private fun stubJson(mapping: MappingBuilder, response: String = "{}") {
        wiremock.stubFor(
            mapping.willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(response)
            )
        )
    }

    private fun stubText(mapping: MappingBuilder, response: String) {
        wiremock.stubFor(
            mapping.willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                    .withBody(response)
            )
        )
    }

    private fun stubBinary(mapping: MappingBuilder, response: String) {
        wiremock.stubFor(
            mapping.willReturn(
                WireMock.aResponse()
                    .withStatus(200)
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.valueOf("application/zip").toString())
                    .withBody(response)
            )
        )
    }

    private fun verifyJson(request: RequestPatternBuilder, body: String) {
        wiremock.verify(request.withRequestBody(WireMock.equalToJson(body)))
    }

    companion object {
        private val issueRequest = """
            {"tokenSerialNumber":null,"pkcs10Content":"csr-content"}
        """.trimIndent()

        private val postOperationsRequest = """
            {"requestType":"issueRsaCertificate"}
        """.trimIndent()

        private val putOperationsRequest = """
            {"code":"123456"}
        """.trimIndent()

        private val signSettingsRequest = """
            {"currentSignType":"rsa-certificates"}
        """.trimIndent()

        private val reissueRequest = """
            {"tokenSerialNumber":null,"pkcs10Content":"csr-content","revokeCertificates":[]}
        """.trimIndent()

        private val qesSendRequest = """
            {"type":"enableQesSignType"}
        """.trimIndent()

        private val qesSignRequest = """
            {"type":"enableQesSignType","signature":"signature"}
        """.trimIndent()

        private val poaRequest = """
            {"poaNumber":"00000000-0000-0000-0000-000000000001","issuerInn":"7700000000","confidantInn":"7700000001"}
        """.trimIndent()
    }
}
