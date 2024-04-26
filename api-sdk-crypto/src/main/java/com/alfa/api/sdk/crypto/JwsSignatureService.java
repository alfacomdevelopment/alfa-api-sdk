package com.alfa.api.sdk.crypto;

/**
 * Service interface for signing and verifying JSON Web Signatures (JWS) in both detached and attached modes.
 */
public interface JwsSignatureService {

    /**
     * Signs the provided data in detached mode, generating a JWS signature without embedding the data.
     * <p>
     * The method takes the data to be signed as input and returns the JWS signature as a string.
     * The signature is generated according to the JWS specification, ensuring the integrity and
     * authenticity of the data.
     * </p>
     *
     * @param data The data to be signed.
     * @return The JWS signature as a string.
     */
    String signDetached(String data);

    /**
     * Verifies the provided data against a detached JWS signature.
     * <p>
     * This method takes the original data and the JWS signature (without the data) as inputs.
     * It verifies the signature against the data, returning true if the signature is valid and
     * the data has not been tampered with, and false otherwise.
     * </p>
     *
     * @param data The original data.
     * @param jwsWithoutData The JWS signature without the data.
     * @return true if the signature is valid and the data is intact, false otherwise.
     */
    boolean verifyDetached(String data, String jwsWithoutData);

    /**
     * Signs the provided data in attached mode, generating a JWS signature that includes the data.
     * <p>
     * This method takes the data to be signed as input and returns the JWS signature as a string,
     * with the data embedded within the signature. This mode is useful for compactness and ease of
     * transmission, as the data and its signature are contained within a single string.
     * </p>
     *
     * @param data The data to be signed.
     * @return The JWS signature with the data embedded as a string.
     */
    String signAttached(String data);

    /**
     * Verifies the provided JWS signature with embedded data.
     * <p>
     * This method takes a JWS signature with embedded data as input. It verifies the signature
     * against the embedded data, returning true if the signature is valid and the data has not
     * been tampered with, and false otherwise.
     * </p>
     *
     * @param jwsWithData The JWS signature with the data embedded.
     * @return true if the signature is valid and the data is intact, false otherwise.
     */
    boolean verifyAttached(String jwsWithData);
}

