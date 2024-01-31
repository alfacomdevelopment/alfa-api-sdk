package com.alfa.api.sdk.transactions.model.sber;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Currency account transaction format
 * This parameter is filled in only for currency accounts.
 * <p>
 * {@link #CUR_TRANSFER} - data on the currency account should be transmitted in the same format as data on the ruble account </p>
 * {@link #SWIFT_TRANSFER} - account data should be transmitted in MT103 format.
 */
public enum CurFormat {
    CUR_TRANSFER("curTransfer"), SWIFT_TRANSFER("swiftTransfer");

    private String value;

    CurFormat(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static CurFormat fromValue(String text) {
        for (CurFormat b : CurFormat.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
