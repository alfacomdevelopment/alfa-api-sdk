package com.alfa.api.sdk.transactions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartInfo {
    @JsonProperty("documentCode")
    private String documentCode;

    @JsonProperty("documentContent")
    private String documentContent;

    @JsonProperty("documentDate")
    private String documentDate;

    @JsonProperty("documentNumber")
    private String documentNumber;

    @JsonProperty("paymentNumber")
    private String paymentNumber;

    @JsonProperty("restAmount")
    private String restAmount;
}
