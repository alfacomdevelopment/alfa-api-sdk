package com.alfa.api.sdk.transactions.model.statement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class RurTransfer {
    @JsonProperty("cartInfo")
    private CartInfo cartInfo;

    /**
     * Method of sending the document
     */
    public enum DeliveryKindEnum {
        ELECTRONICALLY("электронно"),

        TELEGRAPH("телеграфом"),

        POST("почтой"),

        URGENT("срочно");

        private String value;

        DeliveryKindEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static DeliveryKindEnum fromValue(String text) {
            for (DeliveryKindEnum b : DeliveryKindEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("deliveryKind")
    private DeliveryKindEnum deliveryKind;

    @JsonProperty("departmentalInfo")
    private DepartmentalInfo departmentalInfo;

    @JsonProperty("payeeAccount")
    private String payeeAccount;

    @JsonProperty("payeeBankBic")
    private String payeeBankBic;

    @JsonProperty("payeeBankCorrAccount")
    private String payeeBankCorrAccount;

    @JsonProperty("payeeBankName")
    private String payeeBankName;

    @JsonProperty("payeeInn")
    private String payeeInn;

    @JsonProperty("payeeKpp")
    private String payeeKpp;

    @JsonProperty("payeeName")
    private String payeeName;

    @JsonProperty("payerAccount")
    private String payerAccount;

    @JsonProperty("payerBankBic")
    private String payerBankBic;

    @JsonProperty("payerBankCorrAccount")
    private String payerBankCorrAccount;

    @JsonProperty("payerBankName")
    private String payerBankName;

    @JsonProperty("payerInn")
    private String payerInn;

    @JsonProperty("payerKpp")
    private String payerKpp;

    @JsonProperty("payerName")
    private String payerName;

    @JsonProperty("payingCondition")
    private String payingCondition;

    /**
     * Code purpose of payment
     */
    public enum PurposeCodeEnum {
        _1("1"),

        _2("2"),

        _3("3");

        private String value;

        PurposeCodeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static PurposeCodeEnum fromValue(String text) {
            for (PurposeCodeEnum b : PurposeCodeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("purposeCode")
    private PurposeCodeEnum purposeCode;

    @JsonProperty("receiptDate")
    private String receiptDate;

    @JsonProperty("valueDate")
    private String valueDate;
}
