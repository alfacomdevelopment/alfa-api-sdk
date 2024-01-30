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

class TransactionsTest : ParentIntegrationTest() {
    @Test
    fun positive() {
        mockGetStatementEndpoint()
        //when
        val result = mockMvc.perform(
            get("/sdk/transactions")
                .param("account", "40702810323180001677")
                .param("date", "2023-01-30")
                .param("page", "1")
                .param("curFormat", "curTransfer")
        )

        // then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("$._links[0].rel").value("prev"))
            .andExpect(jsonPath("$._links[0].href").value("accountNumber=40700010000006103990&statementDate=2018-03-15&page=3"))
            .andExpect(jsonPath("$._links[1].rel").value("prev"))
            .andExpect(jsonPath("$._links[1].href").value("accountNumber=40700010000006103990&statementDate=2018-03-15&page=3"))
            .andExpect(jsonPath("$.transactions[0].documentDate").value("2021-10-07"))
            .andExpect(jsonPath("$.transactions[0].operationDate").value("2018-12-31T00:00:00Z"))
            .andExpect(jsonPath("$.transactions[0].amount.amount").value(1.01))
            .andExpect(jsonPath("$.transactions[0].amount.currencyName").value("USD"))
            .andExpect(jsonPath("$.transactions[0].filial").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.transactions[0].correspondingAccount").value("30101810400000000225"))
            .andExpect(jsonPath("$.transactions[0].operationCode").value("01"))
            .andExpect(jsonPath("$.transactions[0].amountRub.amount").value(1.01))
            .andExpect(jsonPath("$.transactions[0].amountRub.currencyName").value("USD"))
            .andExpect(jsonPath("$.transactions[0].priority").value("5"))
            .andExpect(jsonPath("$.transactions[0].uuid").value("55daccdf-de87-3879-976c-8b8415c8caf9"))
            .andExpect(jsonPath("$.transactions[0].revaln").value("ПК"))
            .andExpect(jsonPath("$.transactions[0].transactionId").value("1211206MOCO#DS0000017"))
            .andExpect(jsonPath("$.transactions[0].debtorCode").value(0))
            .andExpect(jsonPath("$.transactions[0].extendedDebtorCode").value(50012008))
            .andExpect(jsonPath("$.transactions[0].number").value("1843"))
            .andExpect(jsonPath("$.transactions[0].paymentPurpose").value("НДС не облагается"))
            .andExpect(jsonPath("$.transactions[0].direction").value("DEBIT"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payerBankCorrAccount").value("30101810200000000593"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.intermediaryBankOption").value("D"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.intermediaryBankName").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.transactions[0].curTransfer.orderingCustomerAccount").value("/08251801040004813"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.orderingCustomerName").value("ООО Радуга"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.instructionCode").value("instructionCode"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.messageType").value("103"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.exchangeRate").value("67,74"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.beneficiaryBankName").value("АО \"БАНК\""))
            .andExpect(jsonPath("$.transactions[0].curTransfer.messageOriginator").value("TESTT2P"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.receiverCorrespondentAccount").value("30101810400000000000"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.regulatoryReporting").value("/N10/NS/N4/12345678901234567890/N5/12345678901/N6/TP/N7 МS.05.2003/N8/123456789012345/N9/12.05.2003"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.beneficiaryBankAccount").value("TESTTT21323"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.instructedAmount").value("USD70,00"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payeeBankCorrAccount").value("30101810200000000593"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.transactionRelatedReference").value("transactionRelatedReference"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.senderCorrespondentName").value("TESTBANK N.A. NEW YORK,NY"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.senderCorrespondentAccount").value("BOTKGB2L"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.valueDateCurrencyInterbankSettledAmount").value("130824EUR5447,34"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.senderCorrespondentOption").value("D"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.intermediaryBankAccount").value("COBADEFF"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payerInn").value("7720000971"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.senderToReceiverInformation").value("/NZP/OT 15.03.2009. NDS NE OBLAGAETSYA"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.receiverCorrespondentName").value("JSC TESTBANK 3, TESTOVII PEREULOK LONDON UNITED KINGDOM"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payeeBankBic").value("9611925"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.senderCharges").value("USD7,03"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payeeKpp").value("770000001"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payeeAccount").value("40802810401300015422"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.bankOperationCode").value("CRED"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.transactionTypeCode").value("S01"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payeeName").value("Наименование получателя"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.messageReceiveTime").value("15-05-27 13:21"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.messageDestinator").value("TESTV2X"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.messageIdentifier").value("S000013082900014"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.beneficiaryCustomerName").value("ООО Ромашка"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payerName").value("Гаврилов Добрыня Петрович"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.urgent").value("URGENT"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.orderingInstitutionName").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.transactions[0].curTransfer.receiverCorrespondentOption").value("D"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.orderingInstitutionAccount").value("TESTJ080"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.messageSendTime").value("15-05-27 13:21"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.orderingCustomerOption").value("K"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.detailsOfCharges").value("OUR"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.transactionReferenceNumber").value(69528))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payerKpp").value("770000001"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payerBankName").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.transactions[0].curTransfer.orderingInstitutionOption").value("D"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payerAccount").value("40802810401300015422"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payeeBankName").value("АО \"БАНК\""))
            .andExpect(jsonPath("$.transactions[0].curTransfer.beneficiaryBankOption").value("D"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.remittanceInformation").value("PAYMENT ACC AGREEMENT 1 DD 29.11.2018 FOR WATCHES"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.beneficiaryCustomerAccount").value("40702810701300000761"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payerBankBic").value("044525593"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.payeeInn").value("7720000971"))
            .andExpect(jsonPath("$.transactions[0].curTransfer.receiverCharges").value("receiverCharges"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.bankOperationCode").value("CRED"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.intermediaryBankOption").value("D"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.intermediaryBankName").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.orderingCustomerAccount").value("/08251801040004813"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.orderingCustomerName").value("ООО Радуга"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.transactionTypeCode").value("S01"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.instructionCode").value("instructionCode"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.messageReceiveTime").value("15-05-27 13:21"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.messageDestinator").value("TESTV2X"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.messageType").value("103"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.exchangeRate").value("67,74"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.beneficiaryBankName").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.messageIdentifier").value("S000013082900014"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.messageOriginator").value("TESTU2P"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.receiverCorrespondentAccount").value("30101810400000000000"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.regulatoryReporting").value("/N10/NS/N4/12345678901234567890/N5/12345678901/N6/TP/N7 МS.05.2003/N8/123456789012345/N9/12.05.2003"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.beneficiaryBankAccount").value("TEST21323"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.beneficiaryCustomerName").value("ООО Ромашка"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.instructedAmount").value("USD70,00"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.transactionRelatedReference").value("transactionRelatedReference"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.urgent").value("URGENT"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.orderingInstitutionName").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.receiverCorrespondentOption").value("D"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.senderCorrespondentName").value("TESTBANK N.A. NEW YORK,NY"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.orderingInstitutionAccount").value("TESTBJ080"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.senderCorrespondentAccount").value("TESTGB2L"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.valueDateCurrencyInterbankSettledAmount").value("130824EUR5447,34"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.senderCorrespondentOption").value("D"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.messageSendTime").value("15-05-27 13:21"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.orderingCustomerOption").value("K"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.detailsOfCharges").value("OUR"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.intermediaryBankAccount").value("TESTEFF"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.transactionReferenceNumber").value(69528))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.orderingInstitutionOption").value("D"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.senderToReceiverInformation").value("/NZP/OT 15.03.2009. NDS NE OBLAGAETSYA"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.receiverCorrespondentName").value("JSC TESTBANK 3, TESTOVII PEREULOK LONDON UNITED KINGDOM"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.beneficiaryBankOption").value("D"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.remittanceInformation").value("PAYMENT ACC AGREEMENT 1 DD 29.11.2018 FOR WATCHES"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.beneficiaryCustomerAccount").value("40702810701300000761"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.senderCharges").value("USD7,03"))
            .andExpect(jsonPath("$.transactions[0].swiftTransfer.receiverCharges").value("receiverCharges"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payerBankCorrAccount").value("30101810200000000593"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.departmentalInfo.drawerStatus101").value("1"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.departmentalInfo.oktmo").value("11605000"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.departmentalInfo.kbk").value("39210202010061000160"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.departmentalInfo.taxPeriod107").value("МС.03.2016"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.departmentalInfo.docNumber108").value("123"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.departmentalInfo.reasonCode106").value("ТП"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.departmentalInfo.docDate109").value("31.12.2018"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.departmentalInfo.uip").value("32221003200126505006"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.departmentalInfo.paymentKind110").value("1"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payerKpp").value("770000001"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.receiptDate").value("2018-12-31"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.deliveryKind").value("электронно"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payerBankName").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payerInn").value("7720000971"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.valueDate").value("2018-12-31"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.cartInfo.documentCode").value("documentCode"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.cartInfo.documentDate").value("2019-10-19T06:33:47.923Z"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.cartInfo.restAmount").value("restAmount"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.cartInfo.documentNumber").value("documentNumber"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.cartInfo.documentContent").value("documentContent"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.cartInfo.paymentNumber").value("paymentNumber"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payerAccount").value("40802810401300015422"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payeeName").value("Наименование получателя"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payeeBankName").value("АО \"ТЕСТ-БАНК\""))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payeeBankBic").value("9611925"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payerBankBic").value("044525593"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.purposeCode").value("1"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payeeInn").value("7720000971"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payerName").value("Гаврилов Добрыня Петрович"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payeeBankCorrAccount").value("30101810200000000593"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payeeKpp").value("770000001"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payeeAccount").value("40802810401300015422"))
            .andExpect(jsonPath("$.transactions[0].rurTransfer.payingCondition").value("payingCondition"))
            .andReturn()
    }

    @Test
    fun negativeError404() {
        //when
        val result = mockMvc.perform(
            get("/sdk/transactions")
                .param("account", "40702810323180001677")
                .param("date", "2023-01-30")
                .param("page", "1")
                .param("curFormat", "curTransfer")
        )

        // then
        result.andExpect {
            assertTrue(it.resolvedException is SdkException)
            assertTrue(it.resolvedException!!.cause is ApiException)
            val exception = it.resolvedException as SdkException
            val cause = it.resolvedException!!.cause as ApiException
            assertEquals("Error occurred while receiving statement", exception.message)
            assertEquals(404, cause.statusCode)
        }.andReturn()
    }

    @Test
    fun negativeError500() {
        mockGetEndpointWithInternalError("/api/statement/transactions(.+)")

        //when
        val result = mockMvc.perform(
            get("/sdk/transactions")
                .param("account", "40702810323180001677")
                .param("date", "2023-01-30")
                .param("page", "1")
                .param("curFormat", "curTransfer")
        )

        // then
        result.andExpect {
            assertTrue(it.resolvedException is SdkException)
            assertTrue(it.resolvedException!!.cause is ApiException)
            val exception = it.resolvedException as SdkException
            val cause = it.resolvedException!!.cause as ApiException
            assertEquals("Error occurred while receiving statement", exception.message)
            assertEquals(500, cause.statusCode)
            assertEquals("{\"error\": \"Internal Server Error\"}", String(cause.response, StandardCharsets.UTF_8))
        }.andReturn()
    }

    private fun mockGetStatementEndpoint() {
        wiremock.stubFor(
            WireMock.get(WireMock.urlMatching("/api/statement/transactions(.+)"))
                .withQueryParam("accountNumber", WireMock.equalTo("40702810323180001677"))
                .withQueryParam("statementDate", WireMock.equalTo("2023-01-30"))
                .withQueryParam("page", WireMock.equalTo("1"))
                .withQueryParam("curFormat", WireMock.equalTo("curTransfer"))
                .willReturn(
                    WireMock.aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(getMockBodyFromResources("mocks/transactions/statement.json"))
                )
        )
    }
}