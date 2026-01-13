package com.alfa.api.sdk.sample.app.customer.info

import com.alfa.api.sdk.sample.app.ParentIntegrationTest
import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class CustomerInfoControllerTest : ParentIntegrationTest() {
    @Test
    fun positive() {
        mockGetCustomerInfoEndpoint()

        testClient.get()
            .uri("/sdk/customer-info")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.organizationId").isEqualTo("1ba92f29c6a39a60b5bf487a4c8c63631c10c6b3b98e0b7428668838c857b50b")
            .jsonPath("$.fullName").isEqualTo("Test Organization")
            .jsonPath("$.shortName").isEqualTo("Test Org")
            .jsonPath("$.inn").isEqualTo("0000000000")
            .jsonPath("$.organizationForm.code").isEqualTo("00000")
            .jsonPath("$.organizationForm.fullName").isEqualTo("Test Organization Form")
            .jsonPath("$.organizationForm.shortName").isEqualTo("TOF")
            .jsonPath("$.organizationForm.type").isEqualTo("0000")
            .jsonPath("$.kpps[0]").isEqualTo("000000000")
            .jsonPath("$.ogrn").isEqualTo("0000000000000")
            .jsonPath("$.okpo").isEqualTo("00000000")
            .jsonPath("$.okved").isEqualTo("00.00")
            .jsonPath("$.type").isEqualTo("LEGAL")
            .jsonPath("$.phone").isEqualTo("70000000000")
            .jsonPath("$.email").isEqualTo("test@example.com")
            .jsonPath("$.category").isEqualTo("OTHER")
            .jsonPath("$.status").isEqualTo("ACTIVE")
            .jsonPath("$.registrationDate").isEqualTo("2020-01-01T00:00:00Z")
            .jsonPath("$.addresses[0].type").isEqualTo("LEGAL_FULL")
            .jsonPath("$.addresses[0].area").isEqualTo("Test Area")
            .jsonPath("$.addresses[0].building").isEqualTo("1")
            .jsonPath("$.addresses[0].city").isEqualTo("Test City")
            .jsonPath("$.addresses[0].country").isEqualTo("000")
            .jsonPath("$.addresses[0].flat").isEqualTo("15")
            .jsonPath("$.addresses[0].fullAddress").isEqualTo("Test Address")
            .jsonPath("$.addresses[0].house").isEqualTo("73")
            .jsonPath("$.addresses[0].region").isEqualTo("Test Region")
            .jsonPath("$.addresses[0].settlement").isEqualTo("Test Settlement")
            .jsonPath("$.addresses[0].settlementType").isEqualTo("city")
            .jsonPath("$.addresses[0].street").isEqualTo("Test Street")
            .jsonPath("$.addresses[0].zip").isEqualTo("000000")
            .jsonPath("$.accounts[0].number").isEqualTo("00000000000000000000")
            .jsonPath("$.accounts[0].type").isEqualTo("PAYMENT")
            .jsonPath("$.accounts[0].typeName").isEqualTo("Test Account")
            .jsonPath("$.accounts[0].openDate").isEqualTo("2020-01-01")
            .jsonPath("$.accounts[0].currencyCode").isEqualTo("000")
            .jsonPath("$.accounts[0].transitAccountNumber").isEqualTo("")
            .jsonPath("$.accounts[0].specConditions[0].code").isEqualTo("AI11")
            .jsonPath("$.accounts[0].specConditions[0].description").isEqualTo("Нельзя кредитовать")
            .jsonPath("$.accounts[0].specConditions[0].value").isEqualTo(false)
            .jsonPath("$.accounts[0].specConditions[1].code").isEqualTo("AI12")
            .jsonPath("$.accounts[0].specConditions[1].description").isEqualTo("Нельзя дебетовать")
            .jsonPath("$.accounts[0].specConditions[1].value").isEqualTo(false)
            .jsonPath("$.accounts[0].specConditions[2].code").isEqualTo("AI14")
            .jsonPath("$.accounts[0].specConditions[2].description").isEqualTo("Клиент закрыт")
            .jsonPath("$.accounts[0].specConditions[2].value").isEqualTo(false)
            .jsonPath("$.accounts[0].specConditions[3].code").isEqualTo("AI17")
            .jsonPath("$.accounts[0].specConditions[3].description").isEqualTo("Счёт заблокирован")
            .jsonPath("$.accounts[0].specConditions[3].value").isEqualTo(false)
            .jsonPath("$.accounts[0].specConditions[4].code").isEqualTo("AI20")
            .jsonPath("$.accounts[0].specConditions[4].description").isEqualTo("Неактивный счёт")
            .jsonPath("$.accounts[0].specConditions[4].value").isEqualTo(false)
            .jsonPath("$.accounts[0].specConditions[5].code").isEqualTo("AI30")
            .jsonPath("$.accounts[0].specConditions[5].description").isEqualTo("Счёт закрыт")
            .jsonPath("$.accounts[0].specConditions[5].value").isEqualTo(false)
            .jsonPath("$.accounts[0].specConditions[6].code").isEqualTo("AI47")
            .jsonPath("$.accounts[0].specConditions[6].description").isEqualTo("Внутренний счёт")
            .jsonPath("$.accounts[0].specConditions[6].value").isEqualTo(false)
            .jsonPath("$.accounts[0].specConditions[7].code").isEqualTo("AI82")
            .jsonPath("$.accounts[0].specConditions[7].description").isEqualTo("Дебетование ограничено")
            .jsonPath("$.accounts[0].specConditions[7].value").isEqualTo(false)
            .jsonPath("$.accounts[0].specConditions[8].code").isEqualTo("AI83")
            .jsonPath("$.accounts[0].specConditions[8].description").isEqualTo("Электронная картотека")
            .jsonPath("$.accounts[0].specConditions[8].value").isEqualTo(false)
            .jsonPath("$.accounts[0].specConditions[9].code").isEqualTo("AI87")
            .jsonPath("$.accounts[0].specConditions[9].description").isEqualTo("Бумажная разновалютная картотека")
            .jsonPath("$.accounts[0].specConditions[9].value").isEqualTo(false)
            .jsonPath("$.accounts[0].clientName").isEqualTo("")
            .jsonPath("$.accounts[0].amountBalance").isEqualTo(1000.0)
            .jsonPath("$.accounts[0].amountTotal").isEqualTo(1000.0)
            .jsonPath("$.accounts[0].amountHolds").isEqualTo(0.0)
            .jsonPath("$.accounts[0].bank").exists()
    }

    @Test
    fun negativeError404() {
        testClient.get()
            .uri("/sdk/customer-info")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun negativeError500() {
        mockGetEndpointWithInternalError("/api/jp/v2/customer-info")

        testClient.get()
            .uri("/sdk/customer-info")
            .exchange()
            .expectStatus().is5xxServerError
    }

    private fun mockGetCustomerInfoEndpoint() {
        wiremock.stubFor(
            WireMock.get(WireMock.urlEqualTo("/api/jp/v2/customer-info"))
                .willReturn(
                    WireMock.aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(getMockBodyFromResources("mocks/customer-info/customer-info-v2.json"))
                )
        )
    }
}
