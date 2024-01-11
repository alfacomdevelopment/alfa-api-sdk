package com.alfa.api.sdk.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alfa.api.sdk.client.ApiHttpClient;
import com.alfa.api.sdk.client.dto.ApiResponse;
import com.alfa.api.sdk.client.dto.Method;
import com.alfa.api.sdk.common.exceptions.SdkException;
import com.alfa.api.sdk.transactions.model.CurFormat;
import com.alfa.api.sdk.transactions.model.Statement;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Class provides methods for interacting with a transactions API.
 */
@SuppressWarnings("java:S1075")
public class TransactionsApi {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ApiHttpClient apiHttpClient;
    private String contextPath = "/api/statement";

    /**
     * Constructs a new instance of {@link TransactionsApi}.
     *
     * @param apiHttpClient the HTTP client used for making API calls
     */
    public TransactionsApi(ApiHttpClient apiHttpClient) {
        this.apiHttpClient = apiHttpClient;
    }

    /**
     * Constructs a new instance of {@link TransactionsApi}.
     *
     * @param apiHttpClient the HTTP client used for making API calls
     * @param contextPath   the context path for the API endpoint
     */
    public TransactionsApi(ApiHttpClient apiHttpClient, String contextPath) {
        this.apiHttpClient = apiHttpClient;
        if (contextPath != null && !contextPath.isEmpty()) {
            this.contextPath = contextPath;
        }
    }

    /**
     * Retrieves a statement for a specific account number, statement date, page number, and format.
     *
     * @param accountNumber account number
     * @param statementDate date of transaction execution
     * @param page          requested page number
     * @param curFormat     The format of the currency account transaction.
     * @return statement
     */
    public Statement getStatement(String accountNumber, LocalDate statementDate, Integer page, CurFormat curFormat) {
        try {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("accountNumber", accountNumber);
            queryParams.put("statementDate", statementDate.toString());
            queryParams.put("page", page.toString());
            queryParams.put("curFormat", curFormat.toString());
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, contextPath + "/transactions", queryParams, null, null);
            return mapper.readValue(apiResponse.getResponse(), Statement.class);
        } catch (Exception e) {
            throw new SdkException("Error occurred while receiving statement", e);
        }
    }
}
