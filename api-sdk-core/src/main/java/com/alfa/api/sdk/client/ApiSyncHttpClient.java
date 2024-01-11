package com.alfa.api.sdk.client;

import com.alfa.api.sdk.client.common.Utils;
import com.alfa.api.sdk.client.dto.ApiResponse;
import com.alfa.api.sdk.client.dto.Method;
import com.alfa.api.sdk.client.exceptions.ApiException;
import com.alfa.api.sdk.client.security.CredentialProvider;
import com.alfa.api.sdk.client.security.TransportSecurityProvider;
import lombok.Getter;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class provides an implementation of the {@link ApiHttpClient} interface using synchronous HTTP requests.
 */
@SuppressWarnings("ClassDataAbstractionCoupling")
public class ApiSyncHttpClient implements ApiHttpClient {
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
     * @param baseUrl             the base URL for all API calls
     * @param credentialProvider  the provider for API credentials
     */
    public ApiSyncHttpClient(String baseUrl, CredentialProvider credentialProvider) {
        this.baseUrl = processBaseUrl(baseUrl);
        this.credentialProvider = credentialProvider;
    }

    /**
     * Constructs a new instance of {@link ApiSyncHttpClient} with a specified base URL and a provider for transport security settings.
     *
     * @param baseUrl                    the base URL for all API calls
     * @param transportSecurityProvider  the provider for transport security settings
     */
    public ApiSyncHttpClient(String baseUrl, TransportSecurityProvider transportSecurityProvider) {
        this.baseUrl = processBaseUrl(baseUrl);
        this.transportSecurityProvider = transportSecurityProvider;
    }

    /**
     * Constructs a new instance of {@link ApiSyncHttpClient} with a specified base URL,
     * a provider for API credentials,and a provider for transport security settings.
     *
     * @param baseUrl                     the base URL for all API calls
     * @param transportSecurityProvider   the provider for transport security settings
     * @param credentialProvider          the provider for API credentials
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
        try {
            HttpURLConnection connection = createConnection(method, path, queryParams, headers, body);
            ApiResponse response = getResponse(connection);
            if (Utils.isSuccessfulStatusCode(response.getStatusCode())) {
                return response;
            } else {
                throw new ApiException("Server responded with an unsuccessful status code",
                        response.getStatusCode(), response.getResponse());
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Error occurred while sending request", e);
        }
    }

    private HttpURLConnection createConnection(Method method,
                                               String path,
                                               Map<String, String> queryParams,
                                               Map<String, String> headers,
                                               byte[] body) throws IOException {
        URL url = new URL(buildRequestUri(path, queryParams));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection secured = (HttpsURLConnection) connection;
            if (transportSecurityProvider != null) {
                secured.setSSLSocketFactory(transportSecurityProvider.getContext().getSocketFactory());
            }
        }
        connection.setRequestMethod(method.name());
        if (credentialProvider != null) {
            connection.setRequestProperty("Authorization", credentialProvider.getAuthorization());
        }
        if (headers != null) {
            headers.forEach(connection::setRequestProperty);
        }
        if (body != null) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body);
            }
        }
        return connection;
    }

    private ApiResponse getResponse(HttpURLConnection connection) throws IOException {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(connection.getResponseCode());
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 399) {
            String content = getResponseContent(connection.getInputStream());
            apiResponse.setResponse(content.getBytes(StandardCharsets.UTF_8));
        } else {
            String content = getResponseContent(connection.getErrorStream());
            apiResponse.setResponse(content.getBytes(StandardCharsets.UTF_8));
        }
        connection.disconnect();
        return apiResponse;
    }

    private String getResponseContent(InputStream responseStream) throws IOException {
        String content;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {
            content = in.lines().collect(Collectors.joining("\n"));
        }
        return content;
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
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        return urlBuilder.toString();
    }

    private String processBaseUrl(String baseUrl) {
        return baseUrl.trim().replaceAll("/$", "");
    }
}
