package com.alfa.api.sdk.crypto;

/**
 * Service interface for handling PKCS7 CMS signatures.
 * This interface provides methods for signing and verifying data with both detached and attached signatures.
 */
public interface CmsSignatureService {

    /**
     * Signs the provided data with a detached signature.
     * The method returns the signature as a byte array.
     *
     * @param data The data to be signed.
     * @return The detached signature as a byte array.
     */
    byte[] signDetached(byte[] data);

    /**
     * Verifies a detached signature against the provided data.
     *
     * @param data The original data that was signed.
     * @param signature The detached signature to verify.
     * @return {@code true} if the signature is valid, {@code false} otherwise.
     */
    boolean verifyDetached(byte[] data, byte[] signature);

    /**
     * Signs the provided data with an attached signature.
     * The method returns the data along with the signature, encapsulated in a single byte array.
     *
     * @param data The data to be signed.
     * @return The data with the attached signature as a byte array.
     */
    byte[] signAttached(byte[] data);

    /**
     * Verifies an attached signature against the provided data with signature.
     *
     * @param dataWithSignature The data with the attached signature.
     * @return {@code true} if the signature is valid, {@code false} otherwise.
     */
    boolean verifyAttached(byte[] dataWithSignature);
}
