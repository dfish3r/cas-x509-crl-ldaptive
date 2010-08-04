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

/**
 * Implementation of the {@link org.jasig.cas.server.login.ServiceAccessRequest} that implements the CAS protocol.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class CasServiceAccessRequestImpl extends DefaultLoginRequestImpl implements ServiceAccessRequest {

    private final String serviceId;

    private final boolean postRequest;

    public CasServiceAccessRequestImpl(final String sessionId, final String remoteIpAddress, final boolean forceAuthentication, final boolean passiveAuthentication, final String serviceId, final boolean postRequest) {
        super(sessionId, remoteIpAddress, forceAuthentication, passiveAuthentication, null);
        this.serviceId = serviceId;
        this.postRequest = postRequest;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public String getPassiveAuthenticationRedirectUrl() {
        return this.serviceId;
    }

    public boolean isPostRequest() {
        return this.postRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CasServiceAccessRequestImpl)) return false;
        if (!super.equals(o)) return false;

        CasServiceAccessRequestImpl that = (CasServiceAccessRequestImpl) o;

        if (postRequest != that.postRequest) return false;
        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
        result = 31 * result + (postRequest ? 1 : 0);
        return result;
    }
}