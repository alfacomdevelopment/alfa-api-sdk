package com.alfa.api.sdk.crypto.exceptions;

public class CryptoRuntimeException extends RuntimeException {
    public CryptoRuntimeException() {
    }

    public CryptoRuntimeException(String message) {
        super(message);
    }

    public CryptoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
