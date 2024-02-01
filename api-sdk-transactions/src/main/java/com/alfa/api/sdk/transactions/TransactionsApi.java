package com.alfa.api.sdk.transactions;

import com.alfa.api.sdk.client.ApiHttpClient;
import com.alfa.api.sdk.client.dto.ApiResponse;
import com.alfa.api.sdk.client.dto.Method;
import com.alfa.api.sdk.common.exceptions.SdkException;
import com.alfa.api.sdk.transactions.model.odins.Statement1c;
import com.alfa.api.sdk.transactions.model.sber.CurFormat;
import com.alfa.api.sdk.transactions.model.sber.Statement;
import com.alfa.api.sdk.transactions.model.sber.Summary;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Class provides methods for interacting with a transactions API.
 */
@SuppressWarnings("java:S1075")
public class TransactionsApi {
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final ObjectMapper xmlMapper = new XmlMapper()
            .registerModule(new JaxbAnnotationModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
    private final ApiHttpClient apiHttpClient;
    private String contextPath = "/api";

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
     * Retrieves a statement for a specific account number, statement date, page number and format.
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
            String url = String.format("%s/statement/transactions", contextPath);
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, url, queryParams, null, null);
            return jsonMapper.readValue(apiResponse.getResponse(), Statement.class);
        } catch (Exception e) {
            throw new SdkException("Error occurred while receiving statement", e);
        }
    }

    /**
     * Retrieves a summary for a specific account number and statement date.
     *
     * @param accountNumber account number
     * @param statementDate date of transaction execution
     * @return account summary
     */
    public Summary getSummary(String accountNumber, LocalDate statementDate) {
        try {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("accountNumber", accountNumber);
            queryParams.put("statementDate", statementDate.toString());
            String url = String.format("%s/statement/summary", contextPath);
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, url, queryParams, null, null);
            return jsonMapper.readValue(apiResponse.getResponse(), Summary.class);
        } catch (Exception e) {
            throw new SdkException("Error occurred while receiving account summary", e);
        }
    }

    /**
     * Retrieves a statement for a specific account number, statement date, limit and offset in 1C format.
     *
     * @param accountNumber account number
     * @param executeDate   date of transaction execution
     * @param limit         limit the number of transactions returned
     * @param offset        starting point of the transactions to return
     * @return statement in 1C format
     */
    public Statement1c getStatement1C(String accountNumber, LocalDate executeDate, Integer limit, Integer offset) {
        try {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("executeDate", executeDate.toString());
            queryParams.put("limit", limit.toString());
            queryParams.put("offset", offset.toString());
            String url = String.format("%s/accounts/%s/transactions/1C", contextPath, accountNumber);
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, url, queryParams, null, null);
            return xmlMapper.readValue(apiResponse.getResponse(), Statement1c.class);
        } catch (Exception e) {
            throw new SdkException("Error occurred while receiving statement in 1C format", e);
        }
    }

    /**
     * Retrieves a statement for a specific account number, statement date, limit and offset in MT940 format.
     *
     * @param accountNumber account number
     * @param executeDate   date of transaction execution
     * @param limit         limit the number of transactions returned
     * @param offset        starting point of the transactions to return
     * @return statement in MT940 format
     */
    public String getStatementMT940(String accountNumber, LocalDate executeDate, Integer limit, Integer offset) {
        try {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("executeDate", executeDate.toString());
            queryParams.put("limit", limit.toString());
            queryParams.put("offset", offset.toString());
            String url = String.format("%s/accounts/%s/transactions/MT940", contextPath, accountNumber);
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, url, queryParams, null, null);
            return new String(apiResponse.getResponse(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SdkException("Error occurred while receiving statement in MT940 format", e);
        }
    }
}
