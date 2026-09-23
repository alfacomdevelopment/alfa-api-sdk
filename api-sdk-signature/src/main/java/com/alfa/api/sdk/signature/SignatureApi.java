package com.alfa.api.sdk.signature;

import com.alfa.api.sdk.client.ApiHttpClient;
import com.alfa.api.sdk.client.constants.HttpHeaders;
import com.alfa.api.sdk.client.dto.ApiResponse;
import com.alfa.api.sdk.client.dto.Method;
import com.alfa.api.sdk.common.exceptions.SdkException;
import com.alfa.api.sdk.signature.generated.model.EnableQesResponse;
import com.alfa.api.sdk.signature.generated.model.GetPoaStatus;
import com.alfa.api.sdk.signature.generated.model.RegisterPoaRequest;
import com.alfa.api.sdk.signature.generated.model.RegisterPoaResponse;
import com.alfa.api.sdk.signature.generated.model.RequestData;
import com.alfa.api.sdk.signature.generated.model.RequestIssueRSACertificate;
import com.alfa.api.sdk.signature.generated.model.RequestPostOperations;
import com.alfa.api.sdk.signature.generated.model.RequestPutOperations;
import com.alfa.api.sdk.signature.generated.model.RequestReissueRSACertificate;
import com.alfa.api.sdk.signature.generated.model.ResponseDN;
import com.alfa.api.sdk.signature.generated.model.ResponsePostOperations;
import com.alfa.api.sdk.signature.generated.model.ResponsePutOperations;
import com.alfa.api.sdk.signature.generated.model.ResponseRsaCertificate;
import com.alfa.api.sdk.signature.generated.model.RSACertificate;
import com.alfa.api.sdk.signature.generated.model.RsaCertificates;
import com.alfa.api.sdk.signature.generated.model.SendRequestQes;
import com.alfa.api.sdk.signature.generated.model.SendRequestQesResponse;
import com.alfa.api.sdk.signature.generated.model.SignRequestQes;
import com.alfa.api.sdk.signature.generated.model.SignRequestQesResponse;
import com.alfa.api.sdk.signature.generated.model.SignSettings;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides methods for interacting with the electronic signature API.
 */
@Slf4j
@SuppressWarnings("java:S1075")
public class SignatureApi {
    private static final String SIGNATURE_PATH = "/jp/v3/signature";

    private final ObjectMapper jsonMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    private final ApiHttpClient apiHttpClient;
    private String contextPath = "/api";

    /**
     * Constructs a new instance of {@link SignatureApi}.
     *
     * @param apiHttpClient the HTTP client used for making API calls
     */
    public SignatureApi(ApiHttpClient apiHttpClient) {
        this.apiHttpClient = apiHttpClient;
    }

    /**
     * Constructs a new instance of {@link SignatureApi}.
     *
     * @param apiHttpClient the HTTP client used for making API calls
     * @param contextPath   the context path for the API endpoint
     */
    public SignatureApi(ApiHttpClient apiHttpClient, String contextPath) {
        this.apiHttpClient = apiHttpClient;
        if (contextPath != null && !contextPath.isEmpty()) {
            this.contextPath = normalizeContextPath(contextPath);
        }
    }

    /**
     * Retrieves the distinguished name configuration for CSR generation.
     *
     * @return distinguished name configuration
     */
    public ResponseDN getDn() {
        return sendJson(Method.GET, "/dn", null, ResponseDN.class, "DN");
    }

    /**
     * Retrieves the distinguished name configuration as a .cnf file.
     *
     * @return .cnf configuration content
     */
    public String getDnFile() {
        ApiResponse apiResponse = execute(
                Method.GET,
                "/dn/file",
                null,
                headers(HttpHeaders.Accept.TEXT_PLAIN, false),
                null,
                "DN file"
        );
        return new String(apiResponse.getResponse(), StandardCharsets.UTF_8);
    }

    /**
     * Creates an RSA certificate issuance request.
     *
     * @param request certificate signing request data
     * @return created request
     */
    public ResponseRsaCertificate issueRsaCertificate(RequestIssueRSACertificate request) {
        return sendJson(Method.POST, "/rsa-certificates/requests/issue", request,
                ResponseRsaCertificate.class, "RSA certificate issue request");
    }

    /**
     * Sends an RSA certificate request for signing.
     *
     * @param id      request identifier
     * @param request request type
     * @return signing operation
     */
    public ResponsePostOperations sendRequest(String id, RequestPostOperations request) {
        return sendJson(Method.POST, "/requests/" + id + "/operations", request,
                ResponsePostOperations.class, "request signing operation");
    }

    /**
     * Signs an RSA certificate request with an SMS code.
     *
     * @param id      operation identifier
     * @param request one-time SMS code
     * @return signing result
     */
    public ResponsePutOperations signRequest(String id, RequestPutOperations request) {
        return sendJson(Method.PUT, "/requests/operations/" + id, request,
                ResponsePutOperations.class, "request signing");
    }

    /**
     * Retrieves request data.
     *
     * @param id request identifier
     * @return request data
     */
    public RequestData getRequest(String id) {
        return sendJson(Method.GET, "/requests/" + id, null, RequestData.class, "request data");
    }

    /**
     * Retrieves an RSA certificate by identifier.
     *
     * @param id certificate identifier
     * @return RSA certificate
     */
    public RSACertificate getRsaCertificate(String id) {
        return sendJson(Method.GET, "/rsa-certificates/" + id, null, RSACertificate.class, "RSA certificate");
    }

    /**
     * Retrieves all RSA certificates.
     *
     * @return RSA certificates
     */
    public RsaCertificates getRsaCertificates() {
        return sendJson(Method.GET, "/rsa-certificates", null, RsaCertificates.class, "RSA certificates");
    }

    /**
     * Creates an RSA certificate activation request.
     *
     * @param id certificate identifier
     * @return created request
     */
    public ResponseRsaCertificate activateRsaCertificate(String id) {
        return sendJson(Method.POST, "/rsa-certificates/" + id + "/requests/activation", null,
                ResponseRsaCertificate.class, "RSA certificate activation request");
    }

    /**
     * Creates a request to change the current signing method.
     *
     * @param request new signing method
     * @return created request
     */
    public ResponseRsaCertificate changeSignMethod(SignSettings request) {
        return sendJson(Method.PUT, "/sign-settings", request,
                ResponseRsaCertificate.class, "signing method change request");
    }

    /**
     * Creates a request to switch to another RSA certificate.
     *
     * @param id certificate identifier
     * @return created request
     */
    public ResponseRsaCertificate switchRsaCertificate(String id) {
        return sendJson(Method.POST, "/rsa-certificates/" + id + "/requests/switch", null,
                ResponseRsaCertificate.class, "RSA certificate switch request");
    }

    /**
     * Creates an RSA certificate reissue request.
     *
     * @param id      certificate identifier
     * @param request certificate signing request data
     * @return created request
     */
    public ResponseRsaCertificate reissueRsaCertificate(String id, RequestReissueRSACertificate request) {
        return sendJson(Method.POST, "/rsa-certificates/" + id + "/requests/reissue", request,
                ResponseRsaCertificate.class, "RSA certificate reissue request");
    }

    /**
     * Creates an RSA certificate revocation request.
     *
     * @param id certificate identifier
     * @return created request
     */
    public ResponseRsaCertificate revokeRsaCertificate(String id) {
        return sendJson(Method.POST, "/rsa-certificates/" + id + "/requests/revocation", null,
                ResponseRsaCertificate.class, "RSA certificate revocation request");
    }

    /**
     * Creates a QES connection request.
     *
     * @return created QES request
     */
    public EnableQesResponse createQesRequest() {
        return sendJson(Method.POST, "/qes/requests", null, EnableQesResponse.class, "QES request");
    }

    /**
     * Sends a QES connection request for signing.
     *
     * @param id      request identifier
     * @param request request type
     * @return signing document
     */
    public SendRequestQesResponse sendQesRequest(String id, SendRequestQes request) {
        return sendJson(Method.POST, "/qes/requests/" + id, request,
                SendRequestQesResponse.class, "QES request signing operation");
    }

    /**
     * Signs a QES connection request.
     *
     * @param id      request identifier
     * @param request signature data
     * @return signing result
     */
    public SignRequestQesResponse signQesRequest(String id, SignRequestQes request) {
        return sendJson(Method.PATCH, "/qes/requests/" + id, request,
                SignRequestQesResponse.class, "QES request signing");
    }

    /**
     * Creates a power of attorney registration request.
     *
     * @param request power of attorney data
     * @return created request
     */
    public RegisterPoaResponse registerPowerOfAttorney(RegisterPoaRequest request) {
        return sendJson(Method.POST, "/powers-of-attorney/requests", request,
                RegisterPoaResponse.class, "power of attorney registration request");
    }

    /**
     * Retrieves the registration status of a power of attorney.
     *
     * @param id request identifier
     * @return registration status
     */
    public GetPoaStatus getPowerOfAttorneyStatus(String id) {
        return sendJson(Method.GET, "/powers-of-attorney/requests/" + id + "/status", null,
                GetPoaStatus.class, "power of attorney status");
    }

    /**
     * Downloads a power of attorney file.
     *
     * @param id     request identifier
     * @param accept requested file media type, for example application/pdf or application/zip
     * @return downloaded file bytes
     */
    public byte[] downloadPowerOfAttorney(String id, String accept) {
        if (accept == null || accept.isEmpty()) {
            throw new IllegalArgumentException("accept must not be null or empty");
        }
        return execute(
                Method.GET,
                "/powers-of-attorney/requests/" + id,
                null,
                headers(accept, false),
                null,
                "power of attorney file"
        ).getResponse();
    }

    private <T> T sendJson(Method method,
                           String path,
                           Object request,
                           Class<T> responseType,
                           String operation) {
        try {
            byte[] body = request == null ? null : jsonMapper.writeValueAsBytes(request);
            ApiResponse apiResponse = execute(
                    method,
                    path,
                    null,
                    headers(HttpHeaders.Accept.APPLICATION_JSON, body != null),
                    body,
                    operation
            );
            return jsonMapper.readValue(apiResponse.getResponse(), responseType);
        } catch (SdkException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while parsing {} response: {}", operation, e.getMessage(), e);
            throw new SdkException("Error occurred while parsing " + operation + " response", e);
        }
    }

    private ApiResponse execute(Method method,
                                String path,
                                Map<String, String> queryParams,
                                Map<String, String> headers,
                                byte[] body,
                                String operation) {
        try {
            String url = buildUrl(path);
            log.debug("Request {}", operation);
            ApiResponse apiResponse = apiHttpClient.send(method, url, queryParams, headers, body);
            log.debug("Response {}: status={}, responseLength={}",
                    operation, apiResponse.getStatusCode(), apiResponse.getResponse().length);
            return apiResponse;
        } catch (Exception e) {
            log.error("Error while executing {}: {}", operation, e.getMessage(), e);
            throw new SdkException("Error occurred while executing " + operation, e);
        }
    }

    private Map<String, String> headers(String accept, boolean includeContentType) {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT, accept);
        if (includeContentType) {
            headers.put(HttpHeaders.CONTENT_TYPE, HttpHeaders.Accept.APPLICATION_JSON);
        }
        return headers;
    }

    private String buildUrl(String path) {
        return contextPath + SIGNATURE_PATH + path;
    }

    private String normalizeContextPath(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        return normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }
}
