package com.alfa.api.sdk.crypto.impl;

import com.alfa.api.sdk.crypto.AbstractSignatureService;
import com.alfa.api.sdk.crypto.CmsSignatureService;
import com.alfa.api.sdk.crypto.exceptions.CryptoRuntimeException;
import com.alfa.api.sdk.crypto.model.KeyStoreParameters;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("java:S5164")
public class CmsSignatureServiceImpl extends AbstractSignatureService implements CmsSignatureService {
    private final ThreadLocal<CMSSignedDataGenerator> cmsSignedDataGeneratorThreadLocal = new ThreadLocal<>();
    private final String signedDataEncoding;

    public CmsSignatureServiceImpl(KeyStoreParameters parameters, SignatureAlgorithm signatureAlgorithm) {
        super(parameters, signatureAlgorithm);
        this.signedDataEncoding = null;
    }

    public CmsSignatureServiceImpl(KeyStoreParameters parameters, SignatureAlgorithm signatureAlgorithm, String signedDataEncoding) {
        super(parameters, signatureAlgorithm);
        this.signedDataEncoding = signedDataEncoding;
    }

    @Override
    public byte[] signDetached(byte[] data) {
        return sign(data, false);
    }

    @Override
    public byte[] signAttached(byte[] data) {
        return sign(data, true);
    }

    private byte[] sign(byte[] data, boolean attached) {
        try {
            CMSSignedData signedData = getCmsSignedDataGeneratorThreadLocal()
                    .generate(new CMSProcessableByteArray(data), attached);
            return (signedDataEncoding != null) ?
                    signedData.getEncoded(signedDataEncoding) :
                    signedData.getEncoded();
        } catch (CMSException | OperatorCreationException |
                 CertificateEncodingException | IOException e) {
            throw new CryptoRuntimeException("An error occurred during the signing process", e);
        }
    }

    @Override
    public boolean verifyDetached(byte[] data, byte[] signature) {
        return verifySignature(data, signature, true);
    }

    @Override
    public boolean verifyAttached(byte[] dataWithSignature) {
        return verifySignature(dataWithSignature, null, false);
    }

    @SuppressWarnings("unchecked")
    private boolean verifySignature(byte[] data, byte[] signature, boolean isDetached) {
        try {
            CMSSignedData signedData = getSignedData(data, signature, isDetached);
            SignerInformationStore signerInfoStore = signedData.getSignerInfos();
            for (SignerInformation signer : signerInfoStore.getSigners()) {
                Collection<X509CertificateHolder> certs = signedData.getCertificates().getMatches(signer.getSID());
                if (!tryToVerifyByContainerCerts(signer, certs) && !tryToVerifyByProvidedCert(signer)) return false;
            }
            return true;
        } catch (IOException | CMSException | CertificateException | OperatorCreationException e) {
            return false;
        }
    }

    private CMSSignedDataGenerator getCmsSignedDataGeneratorThreadLocal() throws OperatorCreationException, CertificateEncodingException, CMSException {
        CMSSignedDataGenerator cmsSignedDataGenerator = cmsSignedDataGeneratorThreadLocal.get();
        if (cmsSignedDataGenerator == null) {
            cmsSignedDataGenerator = createCmsSignedDataGenerator();
            cmsSignedDataGeneratorThreadLocal.set(cmsSignedDataGenerator);
        }
        return cmsSignedDataGenerator;
    }

    private CMSSignedDataGenerator createCmsSignedDataGenerator() throws OperatorCreationException, CertificateEncodingException, CMSException {
        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
        ContentSigner signer = new JcaContentSignerBuilder(getBouncyCastleSignatureAlgorithm()).build(privateKey);
        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().build()).build(signer, certificate));
        generator.addCertificates(new JcaCertStore(Collections.singletonList(certificate)));
        return generator;
    }

    private boolean tryToVerifyByProvidedCert(SignerInformation signer) throws CMSException, OperatorCreationException {
        return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build(certificate));
    }

    private boolean tryToVerifyByContainerCerts(SignerInformation signer, Collection<X509CertificateHolder> certs) throws CertificateException, IOException, CMSException, OperatorCreationException {
        for (X509CertificateHolder certHolder : certs) {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(certHolder.getEncoded()));

            if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build(cert))) {
                return true;
            }
        }
        return false;
    }

    private CMSSignedData getSignedData(byte[] data, byte[] signature, boolean isDetached) throws CMSException {
        if (isDetached) {
            return new CMSSignedData(new CMSProcessableByteArray(data), signature);
        } else {
            return new CMSSignedData(data);
        }
    }
}
