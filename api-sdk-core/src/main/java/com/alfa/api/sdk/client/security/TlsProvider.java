package com.alfa.api.sdk.client.security;

import com.alfa.api.sdk.client.exceptions.ApiClientCreationException;
import lombok.Builder;
import lombok.Data;
import com.alfa.api.sdk.client.common.CustomX509KeyManager;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * This class provides an implementation of the {@link TransportSecurityProvider} interface using TLS for transport security.
 */
public class TlsProvider implements TransportSecurityProvider {
    private final SSLContext sslContext;

    /**
     * Constructs a new instance of {@link TlsProvider} with the specified TLS properties.
     *
     * @param properties the TLS properties
     */
    public TlsProvider(TlsProperties properties) {
        this.sslContext = createSslContext(properties);
    }

    @Override
    public SSLContext getContext() {
        return sslContext;
    }

    @SuppressWarnings("java:S3516")
    private SSLContext createSslContext(TlsProperties tlsProperties) {
        if (tlsProperties == null) {
            return null;
        }
        TrustManagerFactory trustManagerFactory = createTrustManagerFactory(tlsProperties);
        CustomX509KeyManager customX509KeyManager = createCustomKeyManager(tlsProperties);
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(
                    new KeyManager[]{customX509KeyManager},
                    trustManagerFactory != null ? trustManagerFactory.getTrustManagers() : null,
                    SecureRandom.getInstanceStrong()
            );
            return context;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new ApiClientCreationException("Received an error when creating SslContext for ApiHttpClient", e);
        }
    }

    private CustomX509KeyManager createCustomKeyManager(TlsProperties tlsProperties) {
        if (tlsProperties.getKeyStorePath() == null
                || tlsProperties.getKeyStorePassword() == null
                || tlsProperties.getPrivateKeyAlias() == null) {
            return null;
        }
        CustomX509KeyManager customX509KeyManager;
        try (InputStream file = Files.newInputStream(Paths.get(tlsProperties.getKeyStorePath()))) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(file, tlsProperties.getKeyStorePassword().toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, tlsProperties.getKeyStorePassword().toCharArray());
            customX509KeyManager = new CustomX509KeyManager(
                    (X509KeyManager) keyManagerFactory.getKeyManagers()[0],
                    tlsProperties.getPrivateKeyAlias()
            );
        } catch (UnrecoverableKeyException
                 | CertificateException
                 | KeyStoreException
                 | IOException
                 | NoSuchAlgorithmException e) {
            throw new ApiClientCreationException("Received an error when creating CustomKeyManager", e);
        }
        return customX509KeyManager;
    }

    private TrustManagerFactory createTrustManagerFactory(TlsProperties tlsProperties) {
        if (tlsProperties.getTrustStorePath() == null || tlsProperties.getTrustStorePassword() == null) {
            return null;
        }
        TrustManagerFactory trustManagerFactory;
        try (InputStream file = Files.newInputStream(Paths.get(tlsProperties.getTrustStorePath()))) {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(file, tlsProperties.getTrustStorePassword().toCharArray());
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new ApiClientCreationException("Received an error when creating TrustManagerFactory", e);
        }
        return trustManagerFactory;
    }

    @Data
    @Builder
    public static class TlsProperties {
        /**
         * The path to the trust store.
         */
        private String trustStorePath;

        /**
         * The password for the trust store.
         */
        private String trustStorePassword;

        /**
         * The path to the key store.
         */
        private String keyStorePath;

        /**
         * The password for the key store.
         */
        private String keyStorePassword;

        /**
         * The alias for the private key.
         */
        private String privateKeyAlias;
    }
}
