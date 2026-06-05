package com.alfa.api.sdk.sample.app.controller

import com.alfa.api.sdk.digital.ruble.DigitalRubleApi
import com.alfa.api.sdk.digital.ruble.generated.model.CertificatesResponse
import com.alfa.api.sdk.digital.ruble.generated.model.StatementRequest
import com.alfa.api.sdk.digital.ruble.generated.model.StatementTransactionsResponse
import com.alfa.api.sdk.digital.ruble.generated.model.WalletContextResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sdk/digital-ruble")
class DigitalRubleController(private val digitalRubleApi: DigitalRubleApi) {
    @GetMapping("/wallet-context")
    fun getWalletContext(): WalletContextResponse = digitalRubleApi.getWalletContext()

    @GetMapping("/certificates")
    fun getCertificates(): CertificatesResponse = digitalRubleApi.getCertificates()

    @PostMapping("/statement/transactions")
    fun getStatementTransactions(@RequestBody request: StatementRequest): StatementTransactionsResponse =
        digitalRubleApi.getStatementTransactions(request)
}
