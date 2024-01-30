package com.alfa.api.sdk.transactions.model.odins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentDataType {

    @XmlElement(name = "DocNo")
    private String docNo;
    @XmlElement(name = "DocDate")
    private String docDate;
    @XmlElement(name = "Sum")
    private BigDecimal sum;
    @XmlElement(name = "Payer")
    private Account payer;
    @XmlElement(name = "Payee")
    private Account payee;


    @XmlElement(name = "PaymentKind")
    @XmlJavaTypeAdapter(PaymentKind.PaymentKindAdapter.class)
    private PaymentKind paymentKind;
    public enum PaymentKind {
        ELECTRONICALLY("электронно"),

        TELEGRAPH("телеграфом"),

        POST("почтой"),

        URGENT("срочно");

        private String value;

        PaymentKind(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static PaymentKind fromValue(String text) {
            for (PaymentKind b : PaymentKind.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        static class PaymentKindAdapter extends XmlAdapter<String, PaymentKind> {

            @Override
            public PaymentKind unmarshal(String v) throws Exception {
                return PaymentKind.fromValue(v);
            }

            @Override
            public String marshal(PaymentKind v) throws Exception {
                return v.toString();
            }
        }
    }

    @XmlElement(name = "TransitionKind")
    private String transitionKind;
    @XmlElement(name = "Priority")
    private String priority;
    @XmlElement(name = "Code")
    private String code;
    @XmlElement(name = "IncomeTypeCode")
    private String incomeTypeCode;
    @XmlElement(name = "Purpose")
    private String purpose;
}
