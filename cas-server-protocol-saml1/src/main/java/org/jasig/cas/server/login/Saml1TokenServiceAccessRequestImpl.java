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

import org.jasig.cas.server.util.XmlMarshallingUtils;
import org.opensaml.saml1.core.Request;

/**
 * Constructs a request used to validate a SAML 1.1 request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class Saml1TokenServiceAccessRequestImpl extends DefaultLoginRequestImpl implements TokenServiceAccessRequest {

    private String requestId;

    private final String token;

    public Saml1TokenServiceAccessRequestImpl(final String samlResponse, final String remoteIpAddress) {
        super(null, remoteIpAddress, false);
        final Request request = XmlMarshallingUtils.unmarshall(samlResponse);
        this.requestId = request.getID();

        if (request.getAssertionArtifacts() != null && !request.getAssertionArtifacts().isEmpty()) {
            this.token = request.getAssertionArtifacts().get(0).getAssertionArtifact();
        } else {
            this.token = null;
        }
    }

    public String getToken() {
        return this.token;
    }

    public boolean isProxiedRequest() {
        return false;
    }

    public boolean isPassiveAuthentication() {
        return false;
    }

    public String getPassiveAuthenticationRedirectUrl() {
        return null;
    }

    public String getServiceId() {
        return null;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public boolean isValid() {
        return true;
    }
}
