package com.alfa.api.sdk.transactions.model.odins;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Pagination {
    @XmlElement(name = "Offset")
    private int offset;
    @XmlElement(name = "Limit")
    private int limit;
    @XmlElement(name = "TotalCount")
    private long totalCount;
}
