package com.alfa.api.sdk.digital.ruble;

import com.alfa.api.sdk.client.ApiHttpClient;
import com.alfa.api.sdk.client.constants.HttpHeaders;
import com.alfa.api.sdk.client.dto.ApiResponse;
import com.alfa.api.sdk.client.dto.Method;
import com.alfa.api.sdk.common.exceptions.SdkException;
import com.alfa.api.sdk.digital.ruble.generated.model.CertificatesResponse;
import com.alfa.api.sdk.digital.ruble.generated.model.StatementRequest;
import com.alfa.api.sdk.digital.ruble.generated.model.StatementTransactionsResponse;
import com.alfa.api.sdk.digital.ruble.generated.model.WalletContextResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides methods for interacting with the digital ruble API.
 */
@Slf4j
@SuppressWarnings("java:S1075")
public class DigitalRubleApi {
    private static final String DIGITAL_RUBLE_PATH = "/jp/v1/digital-ruble";

    private final ObjectMapper jsonMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    private final ApiHttpClient apiHttpClient;
    private String contextPath = "/api";

    /**
     * Constructs a new instance of {@link DigitalRubleApi}.
     *
     * @param apiHttpClient the HTTP client used for making API calls
     */
    public DigitalRubleApi(ApiHttpClient apiHttpClient) {
        this.apiHttpClient = apiHttpClient;
    }

    /**
     * Constructs a new instance of {@link DigitalRubleApi}.
     *
     * @param apiHttpClient the HTTP client used for making API calls
     * @param contextPath the context path for the API endpoint
     */
    public DigitalRubleApi(ApiHttpClient apiHttpClient, String contextPath) {
        this.apiHttpClient = apiHttpClient;
        if (contextPath != null && !contextPath.isEmpty()) {
            this.contextPath = normalizeContextPath(contextPath);
        }
    }

    /**
     * Retrieves the digital ruble wallet context.
     *
     * @return wallet context
     */
    public WalletContextResponse getWalletContext() {
        return sendGet("/wallet-context", WalletContextResponse.class, "wallet context");
    }

    /**
     * Retrieves certificates required for digital ruble operations.
     *
     * @return certificates
     */
    public CertificatesResponse getCertificates() {
        return sendGet("/certificates", CertificatesResponse.class, "certificates");
    }

    /**
     * Retrieves digital ruble statement transactions.
     *
     * @param request statement filters and pagination
     * @return statement transactions
     */
    public StatementTransactionsResponse getStatementTransactions(StatementRequest request) {
        try {
            Map<String, String> headers = jsonHeaders(true);
            String url = buildUrl("/statement/transactions");
            log.debug("Request getStatementTransactions");
            ApiResponse apiResponse = apiHttpClient.send(
                    Method.POST,
                    url,
                    null,
                    headers,
                    jsonMapper.writeValueAsBytes(request)
            );
            log.debug("Response getStatementTransactions: status={}, responseLength={}",
                    apiResponse.getStatusCode(), apiResponse.getResponse().length);
            return jsonMapper.readValue(apiResponse.getResponse(), StatementTransactionsResponse.class);
        } catch (Exception e) {
            log.error("Error while getting digital ruble statement transactions: {}", e.getMessage(), e);
            throw new SdkException("Error occurred while receiving digital ruble statement transactions", e);
        }
    }

    private <T> T sendGet(String path, Class<T> responseType, String responseName) {
        try {
            String url = buildUrl(path);
            log.debug("Request digital ruble {}", responseName);
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, url, null, jsonHeaders(false), null);
            log.debug("Response digital ruble {}: status={}, responseLength={}",
                    responseName, apiResponse.getStatusCode(), apiResponse.getResponse().length);
            return jsonMapper.readValue(apiResponse.getResponse(), responseType);
        } catch (Exception e) {
            log.error("Error while getting digital ruble {}: {}", responseName, e.getMessage(), e);
            throw new SdkException("Error occurred while receiving digital ruble " + responseName, e);
        }
    }

    private Map<String, String> jsonHeaders(boolean includeContentType) {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT, HttpHeaders.Accept.APPLICATION_JSON);
        if (includeContentType) {
            headers.put(HttpHeaders.CONTENT_TYPE, HttpHeaders.Accept.APPLICATION_JSON);
        }
        return headers;
    }

    private String buildUrl(String path) {
        return contextPath + DIGITAL_RUBLE_PATH + path;
    }

    private String normalizeContextPath(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        return normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }
}
