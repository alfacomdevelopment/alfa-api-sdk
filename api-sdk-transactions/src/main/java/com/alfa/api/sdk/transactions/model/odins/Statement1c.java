package com.alfa.api.sdk.transactions.model.odins;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "Response", namespace = "http://directbank.1c.ru/XMLSchema")
@XmlAccessorType(XmlAccessType.FIELD)
public class Statement1c {
    @XmlElement(name = "Pagination")
    private Pagination pagination;

    @XmlElement(name = "Statement")
    private Statement statement;
}
