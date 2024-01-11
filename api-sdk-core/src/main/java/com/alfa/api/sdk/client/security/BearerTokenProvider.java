package com.alfa.api.sdk.client.security;

import lombok.Setter;

/**
 * This class provides an implementation of the {@link CredentialProvider} interface using bearer token for authorization.
 */
@Setter
public class BearerTokenProvider implements CredentialProvider {
    private String accessToken;

    /**
     * Constructs a new instance of {@link BearerTokenProvider} with the specified access token.
     *
     * @param accessToken the access token obtained from AlfaID
     */
    public BearerTokenProvider(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getAuthorization() {
        return "Bearer " + accessToken;
    }
}
