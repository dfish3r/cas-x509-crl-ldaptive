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

import org.apache.commons.lang.text.StrSubstitutor;
import org.jasig.cas.server.login.Saml2ArtifactRequestAccessRequestImpl;
import org.jasig.cas.server.login.TokenServiceAccessRequest;
import org.jasig.cas.server.util.DateParser;
import org.jasig.cas.server.util.SamlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import java.security.PrivateKey;
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

    private static final String TEMPLATE_SAML_RESPONSE = ""
        + "<samlp:Response ID=\"${responseId}\" IssueInstant=\"${issueInstant}\" Version=\"2.0\""
        + " xmlns=\"urn:oasis:names:tc:SAML:2.0:assertion\""
        + " xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\""
        + " xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\">"
        + "<samlp:Status>"
        + "<samlp:StatusCode Value=\"urn:oasis:names:tc:SAML:2.0:status:Success\" />"
        + "</samlp:Status>"
        + "<Assertion ID=\"${assertionId}\""
        + " IssueInstant=\"${assertionIssueInstant}\" Version=\"2.0\""
        + " xmlns=\"urn:oasis:names:tc:SAML:2.0:assertion\">"
        + "<Issuer>${issuer}</Issuer>"
        + "<Subject>"
        + "<NameID Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:emailAddress\">"
        + "${userName}"
        + "</NameID>"
        + "<SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:bearer\">"
        + "<SubjectConfirmationData Recipient=\"${acsUrl}\" NotOnOrAfter=\"${notOnOrAfter}\" InResponseTo=\"${requestId}\" />"
        + "</SubjectConfirmation>"
        + "</Subject>"
        + "<Conditions NotBefore=\"${conditionsNotBefore}\""
        + " NotOnOrAfter=\"${conditionsNotAfter}\">"
        + "<AudienceRestriction>"
        + "<Audience>${acsUrl}</Audience>"
        + "</AudienceRestriction>"
        + "</Conditions>"
        + "<AuthnStatement AuthnInstant=\"${authenticationInstant}\">"
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

    private final String issuer;

    private final DateParser dateParser;

    public Saml2ArtifactRequestAccessImpl(final Session session, final Saml2ArtifactRequestAccessRequestImpl impl, final String issuer, final DateParser dateParser) {
        this.impl = impl;
        this.session = session;
        this.issuer = issuer;
        this.dateParser = dateParser;
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

    protected String signSamlResponse(final String samlResponse, final PrivateKey privateKey, final PublicKey publicKey) {
        final Document doc = SamlUtils.constructDocumentFromXmlString(samlResponse);

        if (doc != null) {
            final Element signedElement = signSamlElement(doc.getDocumentElement(), privateKey, publicKey);
//            doc.setRootElement((Element) signedElement.detach());
            return SamlUtils.createStringFromDocument(doc);
        }
        throw new RuntimeException("Error signing SAML Response: Null document");
    }

    private static Element signSamlElement(final Element element, final PrivateKey privKey, final PublicKey pubKey) {
        try {
            final XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance("DOM");

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

            // Create a DOMSignContext and specify the DSA/RSA PrivateKey and
            // location of the resulting XMLSignature's parent element
            final DOMSignContext dsc = new DOMSignContext(privKey, element);

            final Node xmlSigInsertionPoint = getXmlSignatureInsertLocation(element);
            dsc.setNextSibling(xmlSigInsertionPoint);

            // Marshal, generate (and sign) the enveloped signature
            final XMLSignature signature = sigFactory.newXMLSignature(signedInfo, keyInfo);
            signature.sign(dsc);

            return element;
        } catch (final Exception e) {
            throw new RuntimeException("Error signing SAML element: " + e.getMessage(), e);
        }
    }

    private static Node getXmlSignatureInsertLocation(final Element elem) {
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
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, 1);

        final String userId;

        if (this.impl.getAlternateUserName() == null) {
            userId = this.session.getPrincipal().getName();
        } else {

            final Object attributeValue = this.session.getPrincipal().getAttributeValue(this.impl.getAlternateUserName());
            if (attributeValue == null) {
                userId = this.session.getPrincipal().getName();
            } else {
                userId = attributeValue.toString();
            }
        }

        final Map<String, Object> parameters = new HashMap<String, Object>();
        final String currentDateAndTime = this.dateParser.format(new Date());
        final String endTime = this.dateParser.format(c.getTime());
        parameters.put("responseId", createID());
        parameters.put("issueInstant", currentDateAndTime);
        parameters.put("assertionId", createID());
        parameters.put("assertionIssueInstant", currentDateAndTime);
        parameters.put("issuer", this.issuer);
        parameters.put("userName", userId);
        parameters.put("acsUrl", getId());
        parameters.put("notOnOrAfter", endTime);
        parameters.put("requestId", this.impl.getRequestId());
        parameters.put("conditionsNotBefore", currentDateAndTime);
        parameters.put("conditionsNotAfter", endTime);
        parameters.put("authenticationInstant", this.dateParser.format(this.session.getAuthentications().last().getAuthenticationDate()));

        final StrSubstitutor subs = new StrSubstitutor(parameters);
        return subs.replace(TEMPLATE_SAML_RESPONSE);
    }
}
