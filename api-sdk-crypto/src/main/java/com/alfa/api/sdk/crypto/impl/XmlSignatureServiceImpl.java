package com.alfa.api.sdk.crypto.impl;

import com.alfa.api.sdk.crypto.AbstractSignatureService;
import com.alfa.api.sdk.crypto.XmlSignatureService;
import com.alfa.api.sdk.crypto.exceptions.CryptoRuntimeException;
import com.alfa.api.sdk.crypto.model.KeyStoreParameters;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmlSignatureServiceImpl extends AbstractSignatureService implements XmlSignatureService {
    public XmlSignatureServiceImpl(KeyStoreParameters parameters, SignatureAlgorithm signatureAlgorithm) {
        super(parameters, signatureAlgorithm);
    }

    @Override
    public String sign(String data) {
        try {
            Document document = parseXmlDocument(data);
            SignedInfo signedInfo = createSignedInfo();
            KeyInfo keyInfo = createKeyInfo();

            signDocument(document, signedInfo, keyInfo);

            return serializeXml(document);
        } catch (Exception e) {
            throw new CryptoRuntimeException("An error occurred while signing XML", e);
        }
    }

    @Override
    public boolean verify(String dataWithSignature) {
        try {
            Document document = parseXmlDocument(dataWithSignature);

            NodeList nodeList = document.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
            if (nodeList.getLength() == 0) {
                return false;
            }

            DOMValidateContext validateContext = new DOMValidateContext(new X509KeySelector(), nodeList.item(0));
            validateContext.setProperty("org.jcp.xml.dsig.secureValidation", Boolean.TRUE);
            XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");
            XMLSignature signature = xmlSignatureFactory.unmarshalXMLSignature(validateContext);
            return signature.validate(validateContext);
        } catch (Exception e) {
            return false;
        }
    }

    private String serializeXml(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter stringWriter = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));

        return stringWriter.toString();
    }

    private void signDocument(Document document, SignedInfo signedInfo, KeyInfo keyInfo) throws MarshalException, XMLSignatureException {
        DOMSignContext domSignContext = new DOMSignContext(privateKey, document.getDocumentElement());

        XMLSignatureFactory.getInstance("DOM")
                .newXMLSignature(signedInfo, keyInfo)
                .sign(domSignContext);
    }

    private KeyInfo createKeyInfo() {
        KeyInfoFactory keyInfoFactory = XMLSignatureFactory.getInstance("DOM").getKeyInfoFactory();
        List<Object> x509Content = new ArrayList<>();
        x509Content.add(certificate.getSubjectX500Principal().getName());
        x509Content.add(certificate);
        X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
        return keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
    }

    private SignedInfo createSignedInfo() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Reference ref = XMLSignatureFactory.getInstance("DOM").newReference("",
                XMLSignatureFactory.getInstance("DOM").newDigestMethod(DigestMethod.SHA256, null),
                Collections.singletonList(XMLSignatureFactory.getInstance("DOM").newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                null, null);
        return XMLSignatureFactory.getInstance("DOM")
                .newSignedInfo(
                        XMLSignatureFactory.getInstance("DOM")
                                .newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                        XMLSignatureFactory.getInstance("DOM")
                                .newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", null),
                        Collections.singletonList(ref)
                );
    }

    private Document parseXmlDocument(String data) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return documentBuilderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(data)));
    }

    static class X509KeySelector extends KeySelector {
        public KeySelectorResult select(KeyInfo keyInfo, KeySelector.Purpose purpose, AlgorithmMethod method, XMLCryptoContext context)
                throws KeySelectorException {
            for (XMLStructure xmlStructure : keyInfo.getContent()) {
                if (!(xmlStructure instanceof X509Data))
                    continue;
                X509Data x509Data = (X509Data) xmlStructure;
                for (Object certObject : x509Data.getContent()) {
                    if (!(certObject instanceof X509Certificate))
                        continue;
                    X509Certificate cert = (X509Certificate) certObject;
                    PublicKey key = cert.getPublicKey();
                    return new SimpleKeySelectorResult(key);
                }
            }
            throw new KeySelectorException("No key found!");
        }
    }

    @Getter
    static class SimpleKeySelectorResult implements KeySelectorResult {
        private final Key key;

        public SimpleKeySelectorResult(Key key) {
            this.key = key;
        }

    }
}
