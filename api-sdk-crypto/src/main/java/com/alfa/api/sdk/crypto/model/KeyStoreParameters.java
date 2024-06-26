package com.alfa.api.sdk.crypto.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeyStoreParameters {
    /**
     * The path to the key store.
     */
    private String path;

    /**
     * Key store type.
     */
    private KeyStoreType type;

    /**
     * The password for the key store.
     */
    private String password;

    /**
     * Private key parameters.
     */
    private PrivateKeyParameters privateKey;

    /**
     * Certificate parameters.
     */
    private CertificateParameters certificate;

    @Data
    @Builder
    public static class PrivateKeyParameters {
        /**
         * Private key alias.
         */
        private String alias;
        /**
         * Private key password.
         */
        private String password;
    }

    @Data
    @Builder
    public static class CertificateParameters {
        /**
         * Certificate alias.
         */
        private String alias;
    }

    public enum KeyStoreType {
        JKS,
        JCEKS,
        PKCS12
    }
}
