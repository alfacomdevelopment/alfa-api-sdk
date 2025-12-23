package com.alfa.api.sdk.sample.app.controller

import com.alfa.api.sdk.transactions.TransactionsApi
import com.alfa.api.sdk.transactions.statement.generated.model.CurFormat
import com.alfa.api.sdk.transactions.statement.generated.model.StatementResponse
import com.alfa.api.sdk.transactions.statement1c.generated.model.Statement1cResponse
import com.alfa.api.sdk.transactions.summary.generated.model.SummaryResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
    ): StatementResponse {
        return transactionsApi.getStatement(account, date, page, CurFormat.fromValue(curFormat))
    }

    @GetMapping("/summary")
    fun getSummary(
        @RequestParam account: String,
        @RequestParam date: LocalDate
    ): SummaryResponse {
        return transactionsApi.getSummary(account, date)
    }

    @GetMapping("/1C")
    fun getTransactions1C(
        @RequestParam account: String,
        @RequestParam date: LocalDate,
        @RequestParam limit: Int,
        @RequestParam offset: Int
    ): Statement1cResponse {
        return transactionsApi.getStatement1C(account, date, limit, offset)
    }

    @GetMapping("/MT940")
    fun getTransactionsMT940(
        @RequestParam account: String,
        @RequestParam date: LocalDate,
        @RequestParam limit: Int,
        @RequestParam offset: Int
    ): String {
        return transactionsApi.getStatementMT940(account, date, limit, offset)
    }
}