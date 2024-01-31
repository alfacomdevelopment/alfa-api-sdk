package com.alfa.api.sdk.transactions.model.odins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class StatementData {
    @XmlElement(name = "StatementType")
    @XmlJavaTypeAdapter(StatementType.StatementTypeAdapter.class)
    private StatementType statementType;

    public enum StatementType {
        ZERO("0"),

        ONE("1"),

        TWO("2");

        private String value;

        StatementType(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static StatementType fromValue(String text) {
            for (StatementType b : StatementType.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        static class StatementTypeAdapter extends XmlAdapter<String, StatementType> {

            @Override
            public StatementType unmarshal(String v) throws Exception {
                return StatementType.fromValue(v);
            }

            @Override
            public String marshal(StatementType v) throws Exception {
                return v.toString();
            }
        }
    }
    @XmlElement(name = "DateFrom")
    private String dateFrom;
    @XmlElement(name = "DateTo")
    private String dateTo;
    @XmlElement(name = "Account")
    private String account;
    @XmlElement(name = "Bank")
    private Bank bank;
    @XmlElement(name = "OpeningBalance")
    private BigDecimal openingBalance;
    @XmlElement(name = "TotalDebits")
    private BigDecimal totalDebits;
    @XmlElement(name = "TotalCredits")
    private BigDecimal totalCredits;
    @XmlElement(name = "ClosingBalance")
    private BigDecimal closingBalance;
    @XmlElement(name = "OperationInfo")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<OperationInfo> operationInfo;
    @XmlElement(name = "Stamp")
    private StatementDataStamp stamp;
}

