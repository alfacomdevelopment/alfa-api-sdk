package com.alfa.api.sdk.transactions.model.odins;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Sender {
    @XmlAttribute
    private String bic;
    @XmlAttribute
    private String name;
}
