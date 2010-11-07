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
import org.springframework.util.Assert;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;

/**
 * Default implementation of the {@link org.jasig.cas.server.login.LoginRequest} interface.  This implements the
 * common functionality of the LoginRequest.
 * <p>
 * Protocol specific versions of the LoginRequest would include the additional items that are specific to the
 * protocol that is being implemented.
 * <p>
 * As currently implemented, this class is mutable.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class DefaultLoginRequestImpl implements LoginRequest {

    private final List<Credential> credentials = new ArrayList<Credential>();

    private final Date date = new Date();

    private final boolean forceAuthentication;

    private final String remoteIpAddress;

    private String sessionId;

    private boolean longTermLoginRequest;

    private final Access access;

    public DefaultLoginRequestImpl(final String sessionId, final String remoteIpAddress, final boolean forceAuthentication, final Access access) {
        Assert.notNull(remoteIpAddress, "remoteIpAddress cannot be null");
        if (sessionId == null || sessionId.isEmpty()) {
            this.sessionId = null;
        } else {
            this.sessionId = sessionId;
        }
        this.forceAuthentication = forceAuthentication;
        this.remoteIpAddress = remoteIpAddress;
        this.access = access;
    }

    public final List<Credential> getCredentials() {
        return this.credentials;
    }

    public final Date getDate() {
        return new Date(this.date.getTime());
    }

    public final boolean isForceAuthentication() {
        return this.forceAuthentication;
    }

    public final String getRemoteIpAddress() {
        return this.remoteIpAddress;
    }

    public final String getSessionId() {
        return this.sessionId;
    }

    public final void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }


    public final boolean isAccessRequest() {
        return this instanceof ServiceAccessRequest;
    }

    public final void setLongTermLoginRequest(final boolean longTermLoginRequest) {
        this.longTermLoginRequest = longTermLoginRequest;
    }

    public final boolean isLongTermLoginRequest() {
        return this.longTermLoginRequest;
    }

    public final Access getOriginalAccess() {
        return this.access;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultLoginRequestImpl)) return false;

        DefaultLoginRequestImpl that = (DefaultLoginRequestImpl) o;

        if (forceAuthentication != that.forceAuthentication) return false;
        if (longTermLoginRequest != that.longTermLoginRequest) return false;
        if (access != null ? !access.equals(that.access) : that.access != null) return false;
        if (credentials != null ? !credentials.equals(that.credentials) : that.credentials != null) return false;
        if (remoteIpAddress != null ? !remoteIpAddress.equals(that.remoteIpAddress) : that.remoteIpAddress != null)
            return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = credentials != null ? credentials.hashCode() : 0;
        result = 31 * result + (forceAuthentication ? 1 : 0);
        result = 31 * result + (remoteIpAddress != null ? remoteIpAddress.hashCode() : 0);
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        result = 31 * result + (longTermLoginRequest ? 1 : 0);
        result = 31 * result + (access != null ? access.hashCode() : 0);
        return result;
    }
}
