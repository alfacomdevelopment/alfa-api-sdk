package com.alfa.api.sdk.client.security;

import lombok.Setter;

/**
 * This class provides an implementation of the {@link CredentialProvider} interface using API key for authorization.
 */
@Setter
public class ApiKeyProvider implements CredentialProvider {
    private String apiKey;

    /**
     * Constructs a new instance of {@link ApiKeyProvider} with the specified API key.
     *
     * @param apiKey the API key used for authorization
     */
    public ApiKeyProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getAuthorization() {
        return "ApiKey " + apiKey;
    }
}
