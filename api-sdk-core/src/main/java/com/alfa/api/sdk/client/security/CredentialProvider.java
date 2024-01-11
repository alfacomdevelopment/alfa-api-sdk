package com.alfa.api.sdk.client.security;

/**
 * This interface defines a contract for providing authorization credentials for API calls.
 */
public interface CredentialProvider {
    /**
     * Returns the authorization string required for API calls.
     *
     * @return the authorization string
     */
    String getAuthorization();
}

