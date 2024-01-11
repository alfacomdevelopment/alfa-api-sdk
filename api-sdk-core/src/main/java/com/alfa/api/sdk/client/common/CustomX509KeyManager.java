package com.alfa.api.sdk.client.common;

import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509KeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class CustomX509KeyManager extends X509ExtendedKeyManager {
    private final X509KeyManager originalX509KeyManager;
    private final String alias;

    public CustomX509KeyManager(X509KeyManager originalX509KeyManager, String alias) {
        this.originalX509KeyManager = originalX509KeyManager;
        this.alias = alias;
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return new String[]{alias};
    }

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        return alias;
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return originalX509KeyManager.getServerAliases(keyType, issuers);
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return originalX509KeyManager.chooseServerAlias(keyType, issuers, socket);
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        return originalX509KeyManager.getCertificateChain(alias);
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        return originalX509KeyManager.getPrivateKey(alias);
    }
}

