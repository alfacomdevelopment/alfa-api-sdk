package com.alfa.api.sdk.sample.app.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.alfa.api.sdk.transactions.TransactionsApi
import com.alfa.api.sdk.transactions.model.odins.Statement1c
import com.alfa.api.sdk.transactions.model.sber.CurFormat
import com.alfa.api.sdk.transactions.model.sber.Statement
import com.alfa.api.sdk.transactions.model.sber.Summary
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

    @GetMapping("/summary")
    fun getSummary(
        @RequestParam account: String,
        @RequestParam date: LocalDate
    ): Summary {
        return transactionsApi.getSummary(account, date)
    }

    @GetMapping("/1C")
    fun getTransactions1C(
        @RequestParam account: String,
        @RequestParam date: LocalDate,
        @RequestParam limit: Int,
        @RequestParam offset: Int
    ): Statement1c {
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