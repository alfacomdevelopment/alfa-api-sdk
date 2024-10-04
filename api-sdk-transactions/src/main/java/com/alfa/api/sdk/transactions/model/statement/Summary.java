package com.alfa.api.sdk.transactions.model.statement;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Summary {

    private String composedDateTime;

    private String lastMovementDate;

    private BigDecimal openingRate;

    private Amount openingBalance;

    private Amount openingBalanceRub;

    private Amount closingBalance;

    private Amount closingBalanceRub;

    private Amount debitTurnover;

    private Amount debitTurnoverRub;

    private long debitTransactionsNumber;

    private Amount creditTurnover;

    private Amount creditTurnoverRub;

    private long creditTransactionsNumber;

}
