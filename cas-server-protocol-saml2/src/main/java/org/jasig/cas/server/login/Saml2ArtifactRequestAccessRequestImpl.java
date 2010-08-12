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

import org.jasig.cas.server.session.Access;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Represents a SAML2 request.  At the moment, this has been tested with:
 * <ul>
 * <li>Google Apps</li>
 * <li>Salesforce.com</li>
 * </ul>
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class Saml2ArtifactRequestAccessRequestImpl extends DefaultLoginRequestImpl implements ServiceAccessRequest {

    private final String serviceId;

    private final String requestId;

    private final String alternateUserName;

    private final String relayState;

    private final PublicKey publicKey;

    private final PrivateKey privateKey;

    public Saml2ArtifactRequestAccessRequestImpl(String sessionId, String remoteIpAddress, boolean forceAuthentication, boolean passiveAuthentication, Access access, final String serviceId, final String requestId, final String alternateUserName, final String relayState, final PrivateKey privateKey, final PublicKey publicKey) {
        super(sessionId, remoteIpAddress, forceAuthentication, passiveAuthentication, access);
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
}
