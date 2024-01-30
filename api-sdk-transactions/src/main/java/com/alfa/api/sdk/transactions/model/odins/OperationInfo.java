package com.alfa.api.sdk.transactions.model.odins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class OperationInfo {
    @XmlElement(name = "PayDoc")
    private PayDoc payDoc;

    @XmlElement(name = "Date")
    private String date;

    @XmlElement(name = "DC")
    @XmlJavaTypeAdapter(DC.DCAdapter.class)
    private DC dc;
    public enum DC {
        DEBIT("1"),
        CREDIT("2");

        private String value;

        DC(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static DC fromValue(String text) {
            for (DC b : DC.values()) {
                if (b.value.equals(text)) {
                    return b;
                }
            }
            return null;
        }

        static class DCAdapter extends XmlAdapter<String, DC> {

            @Override
            public DC unmarshal(String v) throws Exception {
                return DC.fromValue(v);
            }

            @Override
            public String marshal(DC v) throws Exception {
                return v.toString();
            }
        }
    }

    @XmlElement(name = "Stamp")
    private OperationInfoStamp stamp;
}
