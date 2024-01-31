package com.alfa.api.sdk.transactions.model.odins;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Statement {
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String formatVersion;
    @XmlAttribute
    private String creationDate;
    @XmlElement(name = "Sender")
    private Sender sender;
    @XmlElement(name = "Recipient")
    private Recipient recipient;
    @XmlElement(name = "Data")
    private StatementData data;
}
