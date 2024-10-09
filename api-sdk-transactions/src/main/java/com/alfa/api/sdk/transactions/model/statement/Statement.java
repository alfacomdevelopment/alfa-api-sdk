package com.alfa.api.sdk.transactions.model.statement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("MultipleStringLiterals")
public class Statement {
    @JsonProperty("_links")
    private List<Links> links;

    @JsonProperty("transactions")
    private List<Transaction> transactions;

    @Data
    public static class Transaction {
        @JsonProperty("amount")
        private Amount amount;

        @JsonProperty("amountRub")
        private Amount amountRub;

        @JsonProperty("correspondingAccount")
        private String correspondingAccount;

        /**
         * Transaction direction
         */
        public enum DirectionEnum {
            DEBIT("DEBIT"),

            CREDIT("CREDIT");

            private String value;

            DirectionEnum(String value) {
                this.value = value;
            }

            @Override
            @JsonValue
            public String toString() {
                return String.valueOf(value);
            }

            @JsonCreator
            public static DirectionEnum fromValue(String text) {
                for (DirectionEnum b : DirectionEnum.values()) {
                    if (String.valueOf(b.value).equals(text)) {
                        return b;
                    }
                }
                return null;
            }
        }

        @JsonProperty("direction")
        private DirectionEnum direction;

        @JsonProperty("documentDate")
        private String documentDate;

        @JsonProperty("filial")
        private String filial;

        @JsonProperty("number")
        private String number;

        /**
         * Operation type
         */
        public enum OperationCodeEnum {
            PAYMENT("01"),
            CURRENCY_PAYMENT("01"),

            PAYMENT_REQUEST("02"),

            DEBIT_ORDER("03"),
            DEBIT_CACH_CHECK("03"),

            CACH_CONTRIBUTION("04"),
            CREDIT_CACH_CHECK("04"),
            CREDIT_ORDER("04"),

            COLLECTION_LETTER("06"),

            AKKREDITIV("08"),

            MEMORY_ORDER("09"),

            PAYMENT_ORDER("16"),

            BANK_ORDER("17");

            private String value;

            OperationCodeEnum(String value) {
                this.value = value;
            }

            @Override
            @JsonValue
            public String toString() {
                return String.valueOf(value);
            }

            @JsonCreator
            public static OperationCodeEnum fromValue(String text) {
                for (OperationCodeEnum b : OperationCodeEnum.values()) {
                    if (b.value.equals(text)) {
                        return b;
                    }
                }
                return null;
            }
        }

        @JsonProperty("operationCode")
        private OperationCodeEnum operationCode;

        @JsonProperty("operationDate")
        private String operationDate;

        @JsonProperty("paymentPurpose")
        private String paymentPurpose;

        @JsonProperty("priority")
        private String priority;

        /**
         * Sign of revaluation operation (PK - rate translation, DP - revaluation of coverage rubles)
         */
        public enum RevalnEnum {
            PK("ПК"),

            DP("ДП");

            private String value;

            RevalnEnum(String value) {
                this.value = value;
            }

            @Override
            @JsonValue
            public String toString() {
                return String.valueOf(value);
            }

            @JsonCreator
            public static RevalnEnum fromValue(String text) {
                for (RevalnEnum b : RevalnEnum.values()) {
                    if (String.valueOf(b.value).equals(text)) {
                        return b;
                    }
                }
                return null;
            }
        }

        @JsonProperty("revaln")
        private RevalnEnum revaln;

        @JsonProperty("uuid")
        private String uuid;

        @JsonProperty("transactionId")
        private String transactionId;

        @JsonProperty("debtorCode")
        private String debtorCode;

        @JsonProperty("extendedDebtorCode")
        private String extendedDebtorCode;

        @JsonProperty("rurTransfer")
        private RurTransfer rurTransfer;

        @JsonProperty("swiftTransfer")
        private SwiftTransfer swiftTransfer;

        @JsonProperty("curTransfer")
        private CurTransfer curTransfer;
    }
}
