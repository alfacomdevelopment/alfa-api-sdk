package com.alfa.api.sdk.transactions.model.sber;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DepartmentalInfo {
    @JsonProperty("uip")
    private String uip;

    @JsonProperty("drawerStatus101")
    private String drawerStatus101;

    @JsonProperty("kbk")
    private String kbk;

    @JsonProperty("oktmo")
    private String oktmo;

    @JsonProperty("reasonCode106")
    private String reasonCode106;

    @JsonProperty("taxPeriod107")
    private String taxPeriod107;

    @JsonProperty("docNumber108")
    private String docNumber108;

    @JsonProperty("docDate109")
    private String docDate109;

    @JsonProperty("paymentKind110")
    private String paymentKind110;
}
