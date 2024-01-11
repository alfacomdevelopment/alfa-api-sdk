package com.alfa.api.sdk.client.security;

import javax.net.ssl.SSLContext;

/**
 * This interface defines a contract for providing a transport security context for API client.
 */
public interface TransportSecurityProvider {
    /**
     * Returns an SSLContext object that represents the secure socket protocol context.
     *
     * @return an SSLContext object
     */
    SSLContext getContext();
}
