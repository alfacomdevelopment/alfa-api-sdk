package com.alfa.api.sdk.sample.app.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.alfa.api.sdk.transactions.TransactionsApi
import com.alfa.api.sdk.transactions.model.CurFormat
import com.alfa.api.sdk.transactions.model.Statement
import java.time.LocalDate

@RestController
@RequestMapping("/sdk/transactions")
class TransactionsController(private val transactionsApi: TransactionsApi) {
    @GetMapping
    fun getTransactions(
        @RequestParam account: String,
        @RequestParam date: LocalDate,
        @RequestParam page: Int,
        @RequestParam curFormat: String
    ): Statement {
        return transactionsApi.getStatement(account, date, page, CurFormat.fromValue(curFormat))
    }
}