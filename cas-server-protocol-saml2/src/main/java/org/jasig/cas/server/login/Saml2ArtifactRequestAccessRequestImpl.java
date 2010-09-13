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

package org.jasig.cas.server.login;

import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.session.Access;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a SAML2 request.  At the moment, this has been tested with:
 * <ul>
 * <li>Google Apps</li>
 * <li>Salesforce.com</li>
 * <li>Yammer.com</li>
 * </ul>
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@XmlRootElement(name="AuthnRequest", namespace = "samlp")
public final class Saml2ArtifactRequestAccessRequestImpl implements ServiceAccessRequest {

    @XmlAttribute(name="AssertionConsumerServiceURL")
    private String serviceId;

    @XmlAttribute(name="ID")
    private String requestId;

    @XmlAttribute(name="IssueInstant")
    private Date issueInstant;

    @XmlAttribute(name="ProviderName")
    private String providerName;

    @XmlElement(name="Issuer")
    private String issuer;

    @XmlAttribute(name="ProtocolBinding")
    private String protocolBinding;

    private String alternateUserName;

    private String relayState;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    private String remoteIpAddress;

    private String sessionId;

    private List<Credential> credentials = new ArrayList<Credential>();

    public Saml2ArtifactRequestAccessRequestImpl() {
        // nothing to do
    }

    public Saml2ArtifactRequestAccessRequestImpl(final String sessionId, final String remoteIpAddress, final String serviceId, final String requestId, final String alternateUserName, final String relayState, final PrivateKey privateKey, final PublicKey publicKey) {
        this.sessionId = sessionId;
        this.remoteIpAddress = remoteIpAddress;
        this.serviceId = serviceId;
        this.requestId = requestId;
        this.alternateUserName = alternateUserName;
        this.relayState = relayState;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }


    public String getRequestId() {
        return this.requestId;
    }

    public String getAlternateUserName() {
        return this.alternateUserName;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public String getRelayState() {
        return this.relayState;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public String getPassiveAuthenticationRedirectUrl() {
        return this.serviceId;
    }

    public List<Credential> getCredentials() {
        return this.credentials;
    }

    public Date getDate() {
        return this.issueInstant;
    }

    public boolean isForceAuthentication() {
        return false;
    }

    public String getRemoteIpAddress() {
        return this.remoteIpAddress;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isPassiveAuthentication() {
        return false;
    }

    public boolean isLongTermLoginRequest() {
        return false;
    }

    public Access getOriginalAccess() {
        return null;
    }

    public void setRelayState(final String relayState) {
        this.relayState = relayState;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public void setServiceId(final String serviceId) {
        this.serviceId = serviceId;
    }

    public String getProviderName() {
        return this.providerName;
    }

    public void setAlternateUserName(final String alternateUserName) {
        this.alternateUserName = alternateUserName;
    }

    public void setRemoteIpAddress(final String remoteIpAddress) {
        this.remoteIpAddress = remoteIpAddress;
    }

    public void setPublicKey(final PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(final PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public void setIssueInstant(final Date issueInstant) {
        this.issueInstant = issueInstant;
    }

    public void setProviderName(final String providerName) {
        this.providerName = providerName;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }

    public void setProtocolBinding(final String protocolBinding) {
        this.protocolBinding = protocolBinding;
    }

    public String getProtocolBinding() {
        return this.protocolBinding;
    }
}
