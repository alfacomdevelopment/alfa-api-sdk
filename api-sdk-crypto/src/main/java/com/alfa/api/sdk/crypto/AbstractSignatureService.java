package com.alfa.api.sdk.crypto;

import com.alfa.api.sdk.crypto.exceptions.CryptoRuntimeException;
import com.alfa.api.sdk.crypto.exceptions.IllegalCryptoArgumentException;
import com.alfa.api.sdk.crypto.model.KeyStoreParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public abstract class AbstractSignatureService {
    protected final PrivateKey privateKey;
    protected final X509Certificate certificate;
    private final SignatureAlgorithm signatureAlgorithm;

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.setProperty("crypto.policy", "unlimited");
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    protected AbstractSignatureService(KeyStoreParameters parameters, SignatureAlgorithm signatureAlgorithm) {
        KeyStore keyStore = loadKeyStore(parameters);
        this.signatureAlgorithm = signatureAlgorithm;
        try {
            privateKey = (PrivateKey) keyStore.getKey(parameters.getPrivateKey().getAlias(), parameters.getPassword().toCharArray());
            if (privateKey == null) {
                throw new IllegalCryptoArgumentException(String.format("Private key with alias '%s' was not found in KeyStore", parameters.getPrivateKey().getAlias()));
            }

            certificate = (X509Certificate) keyStore.getCertificate(parameters.getCertificate().getAlias());
            if (certificate == null) {
                throw new IllegalCryptoArgumentException(String.format("Certificate with alias '%s' was not found in KeyStore", parameters.getCertificate().getAlias()));
            }
        } catch (UnrecoverableKeyException cause) {
            throw new IllegalCryptoArgumentException("An error occurred in the process of decrypting a private key when retrieving it from KeyStore", cause);
        } catch (NoSuchAlgorithmException | KeyStoreException cause) {
            throw new CryptoRuntimeException("An error occurred while working with KeyStore", cause);
        }
    }

    protected String getBouncyCastleSignatureAlgorithm() {
        if (signatureAlgorithm == SignatureAlgorithm.RSA) {
            return "SHA256withRSA";
        }
        throw new UnsupportedOperationException("Unsupported signature algorithm: " + signatureAlgorithm);
    }

    public enum SignatureAlgorithm {
        RSA
    }

    private KeyStore loadKeyStore(KeyStoreParameters parameters) {
        try (InputStream inputStream = Files.newInputStream(Paths.get(parameters.getPath()))) {
            KeyStore keyStore = KeyStore.getInstance(parameters.getType().name());
            keyStore.load(inputStream, parameters.getPassword().toCharArray());
            return keyStore;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException cause) {
            throw new CryptoRuntimeException("An error occurred during the KeyStore loading process", cause);
        }
    }
}
