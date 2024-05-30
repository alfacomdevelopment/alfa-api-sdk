package com.alfa.api.sdk.crypto;

/**
 * Service interface for signing XML documents and verifying signatures.
 */
public interface XmlSignatureService {

    /**
     * Signs the given data using XML digital signature.
     *
     * @param data The data to be signed. This should be the XML content that needs to be digitally signed.
     * @return A {@code String} representing the signed data, including the signature itself embedded within the XML structure.
     */
    String sign(String data);

    /**
     * Verifies the signature of the given data against the expected signature.
     *
     * @param dataWithSignature The data that includes the signature to be verified. This should be the XML content
     *                          with the signature embedded within it.
     * @return {@code true} if the signature is valid according to the verification process, {@code false} otherwise.
     */
    boolean verify(String dataWithSignature);
}

