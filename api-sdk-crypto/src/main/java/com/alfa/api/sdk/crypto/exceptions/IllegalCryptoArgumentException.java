package com.alfa.api.sdk.crypto.exceptions;

public class IllegalCryptoArgumentException extends CryptoRuntimeException {
    public IllegalCryptoArgumentException() {
    }

    public IllegalCryptoArgumentException(String message) {
        super(message);
    }

    public IllegalCryptoArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
