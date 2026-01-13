package com.alfa.api.sdk.customer.info;

import com.alfa.api.sdk.client.ApiHttpClient;
import com.alfa.api.sdk.client.constants.HttpHeaders;
import com.alfa.api.sdk.client.dto.ApiResponse;
import com.alfa.api.sdk.client.dto.Method;
import com.alfa.api.sdk.common.exceptions.SdkException;
import com.alfa.api.sdk.customer.info.v2.generated.model.CustomerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides methods for interacting with the customer info API.
 */
@Slf4j
@SuppressWarnings("java:S1075")
public class CustomerInfoApi {
    private final ObjectMapper jsonMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final ApiHttpClient apiHttpClient;
    private String contextPath = "/api";

    /**
     * Constructs a new instance of {@link CustomerInfoApi}.
     *
     * @param apiHttpClient the HTTP client used for making API calls
     */
    public CustomerInfoApi(ApiHttpClient apiHttpClient) {
        this.apiHttpClient = apiHttpClient;
    }

    /**
     * Constructs a new instance of {@link CustomerInfoApi}.
     *
     * @param apiHttpClient the HTTP client used for making API calls
     * @param contextPath   the context path for the API endpoint
     */
    public CustomerInfoApi(ApiHttpClient apiHttpClient, String contextPath) {
        this.apiHttpClient = apiHttpClient;
        if (contextPath != null && !contextPath.isEmpty()) {
            if (!contextPath.startsWith("/")) {
                contextPath = "/" + contextPath;
            }
            if (contextPath.endsWith("/")) {
                contextPath = contextPath.substring(0, contextPath.length() - 1);
            }
            this.contextPath = contextPath;
        }
    }

    /**
     * Retrieves the organization profile.
     *
     * @return organization profile as a {@link CustomerInfo} instance
     * @throws SdkException if the request fails or the response cannot be parsed
     */
    public CustomerInfo getCustomerInfoV2() {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HttpHeaders.ACCEPT, HttpHeaders.Accept.APPLICATION_JSON);

            String url = String.format("%s/jp/v2/customer-info", contextPath);
            log.debug("Request getCustomerInfoV2");
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, url, null, headers, null);
            log.debug("Response getCustomerInfoV2: status={}, responseLength={}", apiResponse.getStatusCode(), apiResponse.getResponse().length);
            return jsonMapper.readValue(apiResponse.getResponse(), CustomerInfo.class);
        } catch (Exception e) {
            log.error("Error while getting customer info: {}", e.getMessage(), e);
            throw new SdkException("Error occurred while receiving customer info", e);
        }
    }
}
