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

import org.springframework.util.Assert;

/**
 * The default {@link org.jasig.cas.server.login.TokenServiceAccessRequest} implementation.  Used when the underlying
 * protocol implementation is token/IsValid.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class DefaultTokenServiceAccessRequestImpl extends DefaultLoginRequestImpl implements TokenServiceAccessRequest {

    private final String token;

    private final String serviceId;

    public DefaultTokenServiceAccessRequestImpl(final String sessionId, final String remoteIpAddress, final boolean forceAuthentication, final String token, final String serviceId) {
        super(sessionId, remoteIpAddress, forceAuthentication ,null);
        this.serviceId = serviceId;
        this.token = token;
    }

    public final String getToken() {
        return this.token;
    }

    public boolean isPassiveAuthentication() {
        return false;
    }

    public final String getServiceId() {
        return this.serviceId;
    }

    public final String getPassiveAuthenticationRedirectUrl() {
        return null;
    }

    public final String toString() {
        return "token=" + this.token + ",serviceId=" + serviceId;
    }
}
