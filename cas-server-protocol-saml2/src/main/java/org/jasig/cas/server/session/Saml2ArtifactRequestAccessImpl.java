/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.cas.server.session;

import org.jasig.cas.server.login.Saml2ArtifactRequestAccessRequestImpl;
import org.jasig.cas.server.login.TokenServiceAccessRequest;
import org.jasig.cas.server.util.SamlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Node;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class Saml2ArtifactRequestAccessImpl implements Access {

    private static final String JSR_105_PROVIDER = "org.jcp.xml.dsig.internal.dom.XMLDSigRI";

    private static final String SAML_PROTOCOL_NS_URI_V20 = "urn:oasis:names:tc:SAML:2.0:protocol";

    private static final String TEMPLATE_SAML_RESPONSE = "<samlp:Response ID=\"<RESPONSE_ID>\" IssueInstant=\"<ISSUE_INSTANT>\" Version=\"2.0\""
        + " xmlns=\"urn:oasis:names:tc:SAML:2.0:assertion\""
        + " xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\""
        + " xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\">"
        + "<samlp:Status>"
        + "<samlp:StatusCode Value=\"urn:oasis:names:tc:SAML:2.0:status:Success\" />"
        + "</samlp:Status>"
        + "<Assertion ID=\"<ASSERTION_ID>\""
        + " IssueInstant=\"2003-04-17T00:46:02Z\" Version=\"2.0\""
        + " xmlns=\"urn:oasis:names:tc:SAML:2.0:assertion\">"
        + "<Issuer>https://www.opensaml.org/IDP</Issuer>"
        + "<Subject>"
        + "<NameID Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:emailAddress\">"
        + "<USERNAME_STRING>"
        + "</NameID>"
        + "<SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:bearer\">"
        + "<SubjectConfirmationData Recipient=\"<ACS_URL>\" NotOnOrAfter=\"<NOT_ON_OR_AFTER>\" InResponseTo=\"<REQUEST_ID>\" />"
        + "</SubjectConfirmation>"
        + "</Subject>"
        + "<Conditions NotBefore=\"2003-04-17T00:46:02Z\""
        + " NotOnOrAfter=\"<NOT_ON_OR_AFTER>\">"
        + "<AudienceRestriction>"
        + "<Audience><ACS_URL></Audience>"
        + "</AudienceRestriction>"
        + "</Conditions>"
        + "<AuthnStatement AuthnInstant=\"<AUTHN_INSTANT>\">"
        + "<AuthnContext>"
        + "<AuthnContextClassRef>"
        + "urn:oasis:names:tc:SAML:2.0:ac:classes:Password"
        + "</AuthnContextClassRef>"
        + "</AuthnContext>"
        + "</AuthnStatement>"
        + "</Assertion></samlp:Response>";

    private static Random random = new Random();

    private static final char[] charMapping = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p'};

    private Saml2ArtifactRequestAccessRequestImpl impl;

    private boolean used = false;

    private final Session session;

    public Saml2ArtifactRequestAccessImpl(final Session session, final Saml2ArtifactRequestAccessRequestImpl impl) {
        this.impl = impl;
        this.session = session;
    } 

    public String getId() {
        return this.impl.getRequestId();
    }

    public String getResourceIdentifier() {
        return this.impl.getServiceId();
    }

    public void validate(final TokenServiceAccessRequest tokenServiceAccessRequest) {
        throw new UnsupportedOperationException();
    }

    public boolean invalidate() {
        throw new UnsupportedOperationException();
    }

    public boolean isLocalSessionDestroyed() {
        return false;
    }

    public boolean requiresStorage() {
        return false;
    }

    public boolean isUsed() {
        return this.used;
    }

    public synchronized AccessResponseResult generateResponse(final AccessResponseRequest accessResponseRequest) {
        if (this.used) {
            throw new IllegalStateException("This has already been used.");
        }

        this.used = true;

        final Map<String, List<String>> parameters = new HashMap<String, List<String>>();
        final String samlResponse = constructSamlResponse();
        final String signedResponse = signSamlResponse(samlResponse, this.impl.getPrivateKey(), this.impl.getPublicKey());
        parameters.put("SAMLResponse", Arrays.asList(signedResponse));
        parameters.put("RelayState", Arrays.asList(this.impl.getRelayState()));

        return new DefaultAccessResponseResultImpl(AccessResponseResult.Operation.POST, parameters, this.impl.getServiceId(), null);
    }

    public String signSamlResponse(final String samlResponse, final PrivateKey privateKey, final PublicKey publicKey) {
        final Document doc = SamlUtils.constructDocumentFromXmlString(samlResponse);

        if (doc != null) {
            final Element signedElement = signSamlElement(doc.getRootElement(), privateKey, publicKey);
            doc.setRootElement((Element) signedElement.detach());
            return new XMLOutputter().outputString(doc);
        }
        throw new RuntimeException("Error signing SAML Response: Null document");
    }

    private static Element signSamlElement(final Element element, final PrivateKey privKey, final PublicKey pubKey) {
        try {
            final String providerName = System.getProperty("jsr105Provider", JSR_105_PROVIDER);
            final XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());

            final List envelopedTransform = Collections.singletonList(sigFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));

            final Reference ref = sigFactory.newReference("", sigFactory.newDigestMethod(DigestMethod.SHA1, null), envelopedTransform, null, null);

            // Create the SignatureMethod based on the type of key
            final SignatureMethod signatureMethod;
            if (pubKey instanceof DSAPublicKey) {
                signatureMethod = sigFactory.newSignatureMethod(SignatureMethod.DSA_SHA1, null);
            } else if (pubKey instanceof RSAPublicKey) {
                signatureMethod = sigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null);
            } else {
                throw new RuntimeException("Error signing SAML element: Unsupported type of key");
            }

            final CanonicalizationMethod canonicalizationMethod = sigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null);

            // Create the SignedInfo
            final SignedInfo signedInfo = sigFactory.newSignedInfo(canonicalizationMethod, signatureMethod, Collections.singletonList(ref));

            // Create a KeyValue containing the DSA or RSA PublicKey
            final KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();
            final KeyValue keyValuePair = keyInfoFactory.newKeyValue(pubKey);

            // Create a KeyInfo and add the KeyValue to it
            final KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(keyValuePair));
            // Convert the JDOM document to w3c (Java XML signature API requires W3C representation)
            final org.w3c.dom.Element w3cElement = toDom(element);

            // Create a DOMSignContext and specify the DSA/RSA PrivateKey and
            // location of the resulting XMLSignature's parent element
            final DOMSignContext dsc = new DOMSignContext(privKey, w3cElement);

            final org.w3c.dom.Node xmlSigInsertionPoint = getXmlSignatureInsertLocation(w3cElement);
            dsc.setNextSibling(xmlSigInsertionPoint);

            // Marshal, generate (and sign) the enveloped signature
            final XMLSignature signature = sigFactory.newXMLSignature(signedInfo, keyInfo);
            signature.sign(dsc);

            return toJdom(w3cElement);
        } catch (final Exception e) {
            throw new RuntimeException("Error signing SAML element: " + e.getMessage(), e);
        }
    }

    private static org.w3c.dom.Element toDom(final Element element) {
        return toDom(element.getDocument()).getDocumentElement();
    }

    private static org.w3c.dom.Document toDom(final Document doc) {
        try {
            final XMLOutputter xmlOutputter = new XMLOutputter();
            final StringWriter elemStrWriter = new StringWriter();
            xmlOutputter.output(doc, elemStrWriter);
            final byte[] xmlBytes = elemStrWriter.toString().getBytes();
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            return dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xmlBytes));
        } catch (final Exception e) {
            return null;
        }
    }

    private static Node getXmlSignatureInsertLocation(org.w3c.dom.Element elem) {
        org.w3c.dom.Node insertLocation = null;
        org.w3c.dom.NodeList nodeList = elem.getElementsByTagNameNS(SAML_PROTOCOL_NS_URI_V20, "Extensions");
        if (nodeList.getLength() != 0) {
            insertLocation = nodeList.item(nodeList.getLength() - 1);
        } else {
            nodeList = elem.getElementsByTagNameNS(SAML_PROTOCOL_NS_URI_V20, "Status");
            insertLocation = nodeList.item(nodeList.getLength() - 1);
        }
        return insertLocation;
    }

    private static Element toJdom(final org.w3c.dom.Element e) {
        return new DOMBuilder().build(e);
    }

    private static String createID() {
        final byte[] bytes = new byte[20]; // 160 bits
        random.nextBytes(bytes);

        final char[] chars = new char[40];

        for (int i = 0; i < bytes.length; i++) {
          int left = (bytes[i] >> 4) & 0x0f;
          int right = bytes[i] & 0x0f;
          chars[i * 2] = charMapping[left];
          chars[i * 2 + 1] = charMapping[right];
        }

        return String.valueOf(chars);
      }

    private String constructSamlResponse() {
        String samlResponse = TEMPLATE_SAML_RESPONSE;

        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, 1);

        final String userId;

        if (this.impl.getAlternateUserName() == null) {
            userId = this.session.getPrincipal().getName();
        } else {

            final Object attributeValue = this.session.getPrincipal().getAttributeValue(this.impl.getAlternateUserName());;
            if (attributeValue == null) {
                userId = this.session.getPrincipal().getName();
            } else {
                userId = attributeValue.toString();
            }
        }

        samlResponse = samlResponse.replace("<USERNAME_STRING>", userId);
        samlResponse = samlResponse.replace("<RESPONSE_ID>", createID());
        samlResponse = samlResponse.replace("<ISSUE_INSTANT>", SamlUtils.getCurrentDateAndTime());
        samlResponse = samlResponse.replace("<AUTHN_INSTANT>", SamlUtils.getCurrentDateAndTime());
        samlResponse = samlResponse.replaceAll("<NOT_ON_OR_AFTER>", SamlUtils.getFormattedDateAndTime(c.getTime()));
        samlResponse = samlResponse.replace("<ASSERTION_ID>", createID());
        samlResponse = samlResponse.replaceAll("<ACS_URL>", getId());
        samlResponse = samlResponse.replace("<REQUEST_ID>", this.impl.getRequestId());

        return samlResponse;
    }
}
