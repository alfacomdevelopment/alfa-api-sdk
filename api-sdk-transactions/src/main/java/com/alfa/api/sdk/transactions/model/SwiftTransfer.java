package com.alfa.api.sdk.transactions.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class SwiftTransfer {
    /**
     * Bank transaction code (MT103 format)
     */
    public enum BankOperationCodeEnum {
        CRED("CRED");

        private String value;

        BankOperationCodeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static BankOperationCodeEnum fromValue(String text) {
            for (BankOperationCodeEnum b : BankOperationCodeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("bankOperationCode")
    private BankOperationCodeEnum bankOperationCode;

    @JsonProperty("beneficiaryBankAccount")
    private String beneficiaryBankAccount;

    @JsonProperty("beneficiaryBankName")
    private String beneficiaryBankName;

    @JsonProperty("beneficiaryBankOption")
    private String beneficiaryBankOption;

    @JsonProperty("beneficiaryCustomerAccount")
    private String beneficiaryCustomerAccount;

    @JsonProperty("beneficiaryCustomerName")
    private String beneficiaryCustomerName;

    /**
     * Information of costs (MT103 format)
     */
    public enum DetailsOfChargesEnum {
        OUR("OUR");

        private String value;

        DetailsOfChargesEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static DetailsOfChargesEnum fromValue(String text) {
            for (DetailsOfChargesEnum b : DetailsOfChargesEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("detailsOfCharges")
    private DetailsOfChargesEnum detailsOfCharges;

    @JsonProperty("exchangeRate")
    private String exchangeRate;

    @JsonProperty("instructedAmount")
    private String instructedAmount;

    @JsonProperty("instructionCode")
    private String instructionCode;

    @JsonProperty("intermediaryBankAccount")
    private String intermediaryBankAccount;

    @JsonProperty("intermediaryBankName")
    private String intermediaryBankName;

    @JsonProperty("intermediaryBankOption")
    private String intermediaryBankOption;

    @JsonProperty("messageDestinator")
    private String messageDestinator;

    @JsonProperty("messageIdentifier")
    private String messageIdentifier;

    @JsonProperty("messageOriginator")
    private String messageOriginator;

    @JsonProperty("messageReceiveTime")
    private String messageReceiveTime;

    @JsonProperty("messageSendTime")
    private String messageSendTime;

    /**
     * SWIFT message type
     */
    public enum MessageTypeEnum {
        _103("103");

        private String value;

        MessageTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static MessageTypeEnum fromValue(String text) {
            for (MessageTypeEnum b : MessageTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("messageType")
    private MessageTypeEnum messageType;

    @JsonProperty("orderingCustomerAccount")
    private String orderingCustomerAccount;

    @JsonProperty("orderingCustomerName")
    private String orderingCustomerName;

    @JsonProperty("orderingCustomerOption")
    private String orderingCustomerOption;

    @JsonProperty("orderingInstitutionAccount")
    private String orderingInstitutionAccount;

    @JsonProperty("orderingInstitutionName")
    private String orderingInstitutionName;

    @JsonProperty("orderingInstitutionOption")
    private String orderingInstitutionOption;

    @JsonProperty("receiverCharges")
    private String receiverCharges;

    @JsonProperty("receiverCorrespondentAccount")
    private String receiverCorrespondentAccount;

    @JsonProperty("receiverCorrespondentName")
    private String receiverCorrespondentName;

    @JsonProperty("receiverCorrespondentOption")
    private String receiverCorrespondentOption;

    @JsonProperty("regulatoryReporting")
    private String regulatoryReporting;

    @JsonProperty("remittanceInformation")
    private String remittanceInformation;

    @JsonProperty("senderCharges")
    private String senderCharges;

    @JsonProperty("senderCorrespondentAccount")
    private String senderCorrespondentAccount;

    @JsonProperty("senderCorrespondentName")
    private String senderCorrespondentName;

    @JsonProperty("senderCorrespondentOption")
    private String senderCorrespondentOption;

    @JsonProperty("senderToReceiverInformation")
    private String senderToReceiverInformation;

    @JsonProperty("transactionReferenceNumber")
    private String transactionReferenceNumber;

    @JsonProperty("transactionRelatedReference")
    private String transactionRelatedReference;

    @JsonProperty("transactionTypeCode")
    private String transactionTypeCode;

    /**
     * Transaction urgency sign (MT103 format)
     */
    public enum UrgentEnum {
        URGENT("URGENT"),
        NORMAL("NORMAL");

        private String value;

        UrgentEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static UrgentEnum fromValue(String text) {
            for (UrgentEnum b : UrgentEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("urgent")
    private UrgentEnum urgent;

    @JsonProperty("valueDateCurrencyInterbankSettledAmount")
    private String valueDateCurrencyInterbankSettledAmount;
}
