package com.alfa.api.sdk.transactions.model.odins;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BankOrder {
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
    @XmlJavaTypeAdapter(PaymentDataType.PaymentKind.PaymentKindAdapter.class)
    private PaymentDataType.PaymentKind paymentKind;
    @XmlElement(name = "TransitionKind")
    private String transitionKind;
    @XmlElement(name = "Priority")
    private String priority;
    @XmlElement(name = "Code")
    private String code;
    @XmlElement(name = "Purpose")
    private String purpose;
}
