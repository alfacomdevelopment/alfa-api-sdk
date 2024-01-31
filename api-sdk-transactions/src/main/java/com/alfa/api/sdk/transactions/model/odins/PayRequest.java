package com.alfa.api.sdk.transactions.model.odins;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PayRequest {

    @XmlElement(name = "PaymentDataType")
    private PaymentDataType paymentDataType;
    @XmlElement(name = "PaymentCondition")
    private String paymentCondition;
    @XmlElement(name = "AcceptTerm")
    private Integer acceptTerm;
    @XmlElement(name = "DocDispatchDate")
    private String docDispatchDate;
}
