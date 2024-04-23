package com.alfa.api.sdk.crypto;

public interface XmlSignatureService {
    String sign(String data);
    boolean verify(String dataWithSignature);
}

