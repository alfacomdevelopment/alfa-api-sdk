package com.alfa.api.sdk.transactions;

import com.alfa.api.sdk.client.ApiHttpClient;
import com.alfa.api.sdk.client.constants.HttpHeaders;
import com.alfa.api.sdk.client.dto.ApiResponse;
import com.alfa.api.sdk.client.dto.Method;
import com.alfa.api.sdk.common.exceptions.SdkException;
import com.alfa.api.sdk.transactions.statement.generated.model.CurFormat;
import com.alfa.api.sdk.transactions.statement.generated.model.StatementResponse;
import com.alfa.api.sdk.transactions.statement1c.generated.model.Statement1cResponse;
import com.alfa.api.sdk.transactions.summary.generated.model.SummaryResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Class provides methods for interacting with a transactions API.
 */
@Slf4j
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
    public StatementResponse getStatement(String accountNumber, LocalDate statementDate, Integer page, CurFormat curFormat) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HttpHeaders.ACCEPT, HttpHeaders.Accept.APPLICATION_JSON);

            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("accountNumber", accountNumber);
            queryParams.put("statementDate", statementDate.toString());
            queryParams.put("page", page.toString());
            queryParams.put("curFormat", curFormat.getValue());

            String url = String.format("%s/statement/transactions", contextPath);
            log.debug("Request getStatement: accountNumber={}, statementDate={}, page={}, curFormat={}", accountNumber, statementDate, page, curFormat);
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, url, queryParams, headers, null);
            log.debug("Response getStatement: status={}, responseLength={}", apiResponse.getStatusCode(), apiResponse.getResponse().length);
            return jsonMapper.readValue(apiResponse.getResponse(), StatementResponse.class);
        } catch (Exception e) {
            log.error("Error while getting statement: {}", e.getMessage(), e);
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
    public SummaryResponse getSummary(String accountNumber, LocalDate statementDate) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HttpHeaders.ACCEPT, HttpHeaders.Accept.APPLICATION_JSON);

            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("accountNumber", accountNumber);
            queryParams.put("statementDate", statementDate.toString());

            String url = String.format("%s/statement/summary", contextPath);
            log.debug("Request getSummary: accountNumber={}, statementDate={}", accountNumber, statementDate);
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, url, queryParams, headers, null);
            log.debug("Response getSummary: status={}, responseLength={}", apiResponse.getStatusCode(), apiResponse.getResponse().length);
            return jsonMapper.readValue(apiResponse.getResponse(), SummaryResponse.class);
        } catch (Exception e) {
            log.error("Error while getting summary: {}", e.getMessage(), e);
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
    public Statement1cResponse getStatement1C(String accountNumber, LocalDate executeDate, Integer limit, Integer offset) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HttpHeaders.ACCEPT, HttpHeaders.Accept.APPLICATION_XML);

            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("executeDate", executeDate.toString());
            queryParams.put("limit", limit.toString());
            queryParams.put("offset", offset.toString());

            String url = String.format("%s/accounts/%s/transactions/1C", contextPath, accountNumber);
            log.debug("Request getStatement1C: accountNumber={}, executeDate={}, limit={}, offset={}", accountNumber, executeDate, limit, offset);
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, url, queryParams, headers, null);
            log.debug("Response getStatement1C: status={}, responseLength={}", apiResponse.getStatusCode(), apiResponse.getResponse().length);
            return xmlMapper.readValue(apiResponse.getResponse(), Statement1cResponse.class);
        } catch (Exception e) {
            log.error("Error while getting statement 1C: {}", e.getMessage(), e);
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
            Map<String, String> headers = new HashMap<>();
            headers.put(HttpHeaders.ACCEPT, HttpHeaders.Accept.TEXT_PLAIN);

            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("executeDate", executeDate.toString());
            queryParams.put("limit", limit.toString());
            queryParams.put("offset", offset.toString());

            String url = String.format("%s/accounts/%s/transactions/MT940", contextPath, accountNumber);
            log.debug("Request getStatementMT940: accountNumber={}, executeDate={}, limit={}, offset={}", accountNumber, executeDate, limit, offset);
            ApiResponse apiResponse = apiHttpClient.send(Method.GET, url, queryParams, headers, null);
            log.debug("Response getStatementMT940: status={}, responseLength={}", apiResponse.getStatusCode(), apiResponse.getResponse().length);
            return new String(apiResponse.getResponse(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Error while getting statement MT940: {}", e.getMessage(), e);
            throw new SdkException("Error occurred while receiving statement in MT940 format", e);
        }
    }
}
