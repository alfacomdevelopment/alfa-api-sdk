package com.alfa.api.sdk.transactions.model.sber;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Amount {
    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("currencyName")
    private String currencyName;
}
