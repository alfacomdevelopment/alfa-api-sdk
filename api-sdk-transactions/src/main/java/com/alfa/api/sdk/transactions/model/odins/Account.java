package com.alfa.api.sdk.transactions.model.odins;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Account {
    @XmlElement(name = "Name")
    private String name;
    @XmlElement(name = "INN")
    private String inn;
    @XmlElement(name = "KPP")
    private String kpp;
    @XmlElement(name = "Account")
    private String account;
    @XmlElement(name = "Bank")
    private Bank bank;
}
