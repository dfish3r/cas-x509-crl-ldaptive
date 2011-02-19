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

import org.jasig.cas.server.Saml11Profile;
import org.jasig.cas.server.session.Access;

/**
 * Represents a SAML 1.1 request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class Saml11RequestAccessRequestImpl extends DefaultLoginRequestImpl implements ServiceAccessRequest {

    private final String serviceId;

    private final boolean passiveAuthentication;

    private final Saml11Profile profile;

    public Saml11RequestAccessRequestImpl(final String sessionId, final String remoteIpAddress, final boolean forceAuthentication, final boolean passiveAuthentication, final String serviceId, final Saml11Profile profile) {
        super(sessionId, remoteIpAddress, forceAuthentication, null);
        this.serviceId = serviceId;
        this.passiveAuthentication = passiveAuthentication;
        this.profile = profile;
    }

    public boolean isPassiveAuthentication() {
        return this.passiveAuthentication;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public String getPassiveAuthenticationRedirectUrl() {
        return this.passiveAuthentication ? this.serviceId : null;
    }

    public Saml11Profile getProfile() {
        return this.profile;
    }

    public boolean isValid() {
        return true;
    }

    public boolean isProxiedRequest() {
        return false;
    }
}
