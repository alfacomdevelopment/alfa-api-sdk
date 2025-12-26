package com.alfa.api.sdk.crypto.impl;

import com.alfa.api.sdk.crypto.AbstractSignatureService;
import com.alfa.api.sdk.crypto.JwsSignatureService;
import com.alfa.api.sdk.crypto.exceptions.CryptoRuntimeException;
import com.alfa.api.sdk.crypto.model.KeyStoreParameters;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64URL;
import lombok.extern.slf4j.Slf4j;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

@Slf4j
public class JwsSignatureServiceImpl extends AbstractSignatureService implements JwsSignatureService {
    public JwsSignatureServiceImpl(KeyStoreParameters parameters, SignatureAlgorithm signatureAlgorithm) {
        super(parameters, signatureAlgorithm);
    }

    @Override
    public String signAttached(String data) {
        return sign(data, true);
    }

    @Override
    public String signDetached(String data) {
        return sign(data, false);
    }


    private String sign(String data, boolean attached) {
        try {
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .type(JOSEObjectType.JWT)
                    .build();
            JWSObject jwsObject = new JWSObject(header, new Payload(data));
            jwsObject.sign(new RSASSASigner(privateKey));
            if (attached) {
                return jwsObject.serialize();
            } else {
                return String.format("%s..%s", jwsObject.getHeader().toBase64URL(), jwsObject.getSignature().toString());
            }
        } catch (JOSEException e) {
            log.error("Error creating JWS: {}", e.getMessage(), e);
            throw new CryptoRuntimeException("An error occurred while creating JWS", e);
        }
    }

    @Override
    public boolean verifyDetached(String data, String jwsWithoutData) {
        return verifyAttached(jwsWithoutData.replaceFirst("\\.\\.", String.format(".%s.", Base64URL.encode(data))));
    }

    @Override
    public boolean verifyAttached(String jwsWithData) {
        try {
            JWSObject jwsObject = JWSObject.parse(jwsWithData);
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) certificate.getPublicKey());
            return jwsObject.verify(verifier);
        } catch (JOSEException | ParseException e) {
            log.warn("JWS verification failed: {}", e.getMessage());
            return false;
        }
    }
}
