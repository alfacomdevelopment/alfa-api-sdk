package com.alfa.api.sdk.transactions.model.odins;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Bank {
    @XmlElement(name = "BIC")
    private String bic;
    @XmlElement(name = "Name")
    private String name;
    @XmlElement(name = "CorrespAcc")
    private String correspAcc;
}
