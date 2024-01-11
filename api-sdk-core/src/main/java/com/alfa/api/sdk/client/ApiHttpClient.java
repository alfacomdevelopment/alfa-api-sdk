package com.alfa.api.sdk.client;

import com.alfa.api.sdk.client.dto.ApiResponse;
import com.alfa.api.sdk.client.dto.Method;

import java.util.Map;

/**
 * This interface defines the contract for an HTTP client for API calls.
 */
public interface ApiHttpClient {
    /**
     * Sends an HTTP request using the provided method, path, query parameters, headers, and body.
     *
     * @param method The HTTP method to be used for the request.
     * @param path The URL path for the request.
     * @param queryParams A map of query parameters to be included in the request. Each key-value pair represents one parameter.
     * @param headers A map of HTTP headers to be included in the request. Each key-value pair represents one header.
     * @param body The body of the HTTP request. This should be null if the request has no body.
     * @return An {@link ApiResponse} object representing the server's response to the request.
     */
    ApiResponse send(Method method,
                     String path,
                     Map<String, String> queryParams,
                     Map<String, String> headers,
                     byte[] body);
}
