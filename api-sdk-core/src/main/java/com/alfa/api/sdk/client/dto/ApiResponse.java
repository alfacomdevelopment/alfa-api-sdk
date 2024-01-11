package com.alfa.api.sdk.client.dto;

import lombok.Data;

@Data
public class ApiResponse {
    private int statusCode;
    private byte[] response;
}
