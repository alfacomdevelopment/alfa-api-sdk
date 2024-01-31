package com.alfa.api.sdk.transactions.model.odins;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BudgetPaymentInfo {
    @XmlElement(name = "DrawerStatus")
    private String drawerStatus;
    @XmlElement(name = "CBC")
    private String cbc;
    @XmlElement(name = "OKTMO")
    private String oktmo;
    @XmlElement(name = "Reason")
    private String reason;
    @XmlElement(name = "TaxPeriod")
    private String taxPeriod;
    @XmlElement(name = "DocNo")
    private String docNo;
    @XmlElement(name = "DocDate")
    private String docDate;
    @XmlElement(name = "PayType")
    private String payType;
}
