package com.alfa.api.sdk.common.exceptions;

public class SdkException extends RuntimeException {
    public SdkException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
