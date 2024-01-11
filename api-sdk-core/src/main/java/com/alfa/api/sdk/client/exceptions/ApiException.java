package com.alfa.api.sdk.client.exceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("java:S1165")
public class ApiException extends RuntimeException {
    private Integer statusCode;
    private byte[] response;

    public ApiException(String message, int statusCode, byte[] response) {
        super(message + String.format(" [status code: %s]", statusCode));
        this.statusCode = statusCode;
        this.response = response;
    }

    public ApiException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApiException(Exception e) {
        super(e);
    }
}
