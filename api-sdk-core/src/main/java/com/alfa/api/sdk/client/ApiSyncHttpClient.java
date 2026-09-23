package com.alfa.api.sdk.client;

import com.alfa.api.sdk.client.common.Utils;
import com.alfa.api.sdk.client.dto.ApiResponse;
import com.alfa.api.sdk.client.dto.Method;
import com.alfa.api.sdk.client.exceptions.ApiException;
import com.alfa.api.sdk.client.security.CredentialProvider;
import com.alfa.api.sdk.client.security.TransportSecurityProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class provides an implementation of the {@link ApiHttpClient} interface using synchronous HTTP requests.
 */
@Slf4j
@SuppressWarnings("ClassDataAbstractionCoupling")
public class ApiSyncHttpClient implements ApiHttpClient {
    private static final int DEFAULT_CONNECT_TIMEOUT_MS = 15_000;
    private static final int DEFAULT_READ_TIMEOUT_MS = 60_000;

    private final String baseUrl;
    @Getter
    private CredentialProvider credentialProvider;
    private TransportSecurityProvider transportSecurityProvider;

    /**
     * Constructs a new instance of {@link ApiSyncHttpClient} with a specified base URL.
     *
     * @param baseUrl the base URL for all API calls
     */
    public ApiSyncHttpClient(String baseUrl) {
        this.baseUrl = processBaseUrl(baseUrl);
    }

    /**
     * Constructs a new instance of {@link ApiSyncHttpClient} with a specified base URL and a provider for API credentials.
     *
     * @param baseUrl            the base URL for all API calls
     * @param credentialProvider the provider for API credentials
     */
    public ApiSyncHttpClient(String baseUrl, CredentialProvider credentialProvider) {
        this.baseUrl = processBaseUrl(baseUrl);
        this.credentialProvider = credentialProvider;
    }

    /**
     * Constructs a new instance of {@link ApiSyncHttpClient} with a specified base URL and a provider for transport security settings.
     *
     * @param baseUrl                   the base URL for all API calls
     * @param transportSecurityProvider the provider for transport security settings
     */
    public ApiSyncHttpClient(String baseUrl, TransportSecurityProvider transportSecurityProvider) {
        this.baseUrl = processBaseUrl(baseUrl);
        this.transportSecurityProvider = transportSecurityProvider;
    }

    /**
     * Constructs a new instance of {@link ApiSyncHttpClient} with a specified base URL,
     * a provider for API credentials,and a provider for transport security settings.
     *
     * @param baseUrl                   the base URL for all API calls
     * @param transportSecurityProvider the provider for transport security settings
     * @param credentialProvider        the provider for API credentials
     */
    public ApiSyncHttpClient(String baseUrl, TransportSecurityProvider transportSecurityProvider, CredentialProvider credentialProvider) {
        this.baseUrl = processBaseUrl(baseUrl);
        this.transportSecurityProvider = transportSecurityProvider;
        this.credentialProvider = credentialProvider;
    }

    public ApiResponse send(Method method,
                            String path,
                            Map<String, String> queryParams,
                            Map<String, String> headers,
                            byte[] body) {
        long startTime = System.currentTimeMillis();
        log.debug("Sending HTTP request: method={}, path={}", method, path);
        try {
            ApiResponse response = sendHttpRequest(method, path, queryParams, headers, body);
            if (Utils.isSuccessfulStatusCode(response.getStatusCode())) {
                log.debug("HTTP request completed successfully: status={}, durationMs={}",
                        response.getStatusCode(),
                        System.currentTimeMillis() - startTime);
                return response;
            } else {
                log.warn("HTTP request failed: status={}, durationMs={}",
                        response.getStatusCode(),
                        System.currentTimeMillis() - startTime);
                throw new ApiException("Server responded with an unsuccessful status code",
                        response.getStatusCode(), response.getResponse());
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while sending HTTP request", e);
            throw new ApiException("Error occurred while sending request", e);
        }
    }

    private ApiResponse sendHttpRequest(Method method,
                                        String path,
                                        Map<String, String> queryParams,
                                        Map<String, String> headers,
                                        byte[] body) throws Exception {
        HttpRequest.BodyPublisher bodyPublisher = body == null
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofByteArray(body);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(buildRequestUri(path, queryParams)))
                .timeout(Duration.ofMillis(DEFAULT_READ_TIMEOUT_MS))
                .method(method.name(), bodyPublisher);

        if (credentialProvider != null) {
            requestBuilder.header("Authorization", credentialProvider.getAuthorization());
        }
        if (headers != null) {
            headers.forEach(requestBuilder::header);
            if (log.isDebugEnabled()) {
                log.debug("HTTP request headers: {}", formatHeaders(requestBuilder.build().headers().map()));
            }
        }

        HttpResponse<byte[]> response = createHttpClient()
                .send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(response.statusCode());
        apiResponse.setResponse(response.body());
        if (log.isDebugEnabled()) {
            log.debug("HTTP response received: status={}, contentLength={}",
                    response.statusCode(), response.body().length);
            log.debug("HTTP response headers: {}", formatHeaders(response.headers().map()));
        }
        return apiResponse;
    }

    private HttpClient createHttpClient() {
        HttpClient.Builder builder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(DEFAULT_CONNECT_TIMEOUT_MS))
                .version(HttpClient.Version.HTTP_1_1);
        if (transportSecurityProvider != null) {
            SSLContext sslContext = transportSecurityProvider.getContext();
            if (sslContext != null) {
                builder.sslContext(sslContext);
            }
        }
        return builder.build();
    }

    @SuppressWarnings({"java:S1075", "MultipleStringLiterals", "ParameterAssignment"})
    private String buildRequestUri(String path, Map<String, String> queryParams) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        StringBuilder urlBuilder = new StringBuilder(baseUrl + path);
        if (queryParams != null && !queryParams.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                    urlBuilder
                            .append(encode(entry.getKey()))
                            .append("=")
                            .append(encode(entry.getValue()))
                            .append("&");
                }
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        return urlBuilder.toString();
    }

    private String processBaseUrl(String baseUrl) {
        return baseUrl.trim().replaceAll("/$", "");
    }

    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not supported", e);
        }
    }

    private String formatHeaders(Map<String, ? extends Iterable<String>> headers) {
        if (headers == null || headers.isEmpty()) {
            return "<empty>";
        }

        return headers.entrySet().stream()
                .filter(it -> it.getKey() != null)
                .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
                .collect(Collectors.joining(", "));
    }
}
