package com.alfa.api.sdk.transactions.model.odins;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentOrder {

    @XmlElement(name = "PaymentDataType")
    private PaymentDataType paymentDataType;
    @XmlElement(name = "TransitionContent")
    private String transitionContent;
    @XmlElement(name = "PartialPaymentNo")
    private String partialPaymentNo;
    @XmlElement(name = "PartialTransitionKind")
    private String partialTransitionKind;
    @XmlElement(name = "SumResidualPayment")
    private BigDecimal sumResidualPayment;
    @XmlElement(name = "PartialDocNo")
    private String partialDocNo;
    @XmlElement(name = "PartialDocDate")
    private String partialDocDate;
    @XmlElement(name = "BudgetPaymentInfo")
    private BudgetPaymentInfo budgetPaymentInfo;
}
