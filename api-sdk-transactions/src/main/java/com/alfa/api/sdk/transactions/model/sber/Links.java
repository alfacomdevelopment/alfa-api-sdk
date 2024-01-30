package com.alfa.api.sdk.transactions.model.sber;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class Links {
    @JsonProperty("href")
    private String href;

    /** Page sign (prev - previous page, next - next page) */
    public enum RelEnum {
        PREV("prev"),
        NEXT("next");

        private final String value;

        RelEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static RelEnum fromValue(String text) {
            for (RelEnum relEnum : RelEnum.values()) {
                if (String.valueOf(relEnum.value).equals(text)) {
                    return relEnum;
                }
            }
            return null;
        }
    }

    @JsonProperty("rel")
    private RelEnum rel;
}
