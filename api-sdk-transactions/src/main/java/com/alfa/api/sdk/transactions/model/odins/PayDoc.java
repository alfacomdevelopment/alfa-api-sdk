package com.alfa.api.sdk.transactions.model.odins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class PayDoc {
    @XmlAttribute
    private String id;
    @XmlAttribute
    @XmlJavaTypeAdapter(DocKind.DocKindAdapter.class)
    private DocKind docKind;

    public enum DocKind {
        PAYMENT("10"),
        PAYMENT_ORDER("10"),
        PAYMENT_REQUEST("11"),
        MEMORY_ORDER("16"),
        COLLECTION_LETTER("17"),
        BANK_ORDER("18"),
        DEFAULT("18");

        private String value;

        DocKind(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static DocKind fromValue(String text) {
            for (DocKind b : DocKind.values()) {
                if (b.name().equals(text)) {
                    return b;
                }
            }
            return null;
        }

        static class DocKindAdapter extends XmlAdapter<String, DocKind> {

            @Override
            public DocKind unmarshal(String v) throws Exception {
                return DocKind.fromValue(v);
            }

            @Override
            public String marshal(DocKind v) throws Exception {
                return v.toString();
            }
        }
    }

    @XmlElement(name = "PayDocRu")
    private PayDocRu payDocRu;
    @XmlElement(name = "PayRequest")
    private PayRequest payRequest;
    @XmlElement(name = "CollectionOrder")
    private PayDocRu collectionOrder;
    @XmlElement(name = "PaymentOrder")
    private PaymentOrder paymentOrder;
    @XmlElement(name = "BankOrder")
    private BankOrder bankOrder;
    @XmlElement(name = "MemOrder")
    private BankOrder memOrder;
    @XmlElement(name = "ExtID")
    private String extId;
}
