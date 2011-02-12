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

import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.session.*;

import java.util.ArrayList;
import java.util.List;

/**
 * CAS-specific implementation of {@link ServiceAccessResponse}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class CasServiceAccessResponseImpl extends DefaultLoginResponseImpl implements ServiceAccessResponse {

    private final CasServiceAccessRequestImpl casServiceAccessRequest;

    private final Access access;

    private final List<Access> loggedOutAccesses = new ArrayList<Access>();

    public CasServiceAccessResponseImpl(final ServiceAccessRequest serviceAccessRequest) {
        this(serviceAccessRequest, null);
    }

    public CasServiceAccessResponseImpl(final ServiceAccessRequest serviceAccessRequest, final AuthenticationResponse authenticationResponse) {
        super(null, authenticationResponse);
        this.casServiceAccessRequest = (CasServiceAccessRequestImpl) serviceAccessRequest;
        this.access = null;
    }

    public CasServiceAccessResponseImpl(final Session session, final Access access, final AuthenticationResponse authenticationResponse, final List<Access> loggedOutAccesses) {
        super(session, authenticationResponse);
        this.casServiceAccessRequest = null;
        this.access = access;
        this.loggedOutAccesses.addAll(loggedOutAccesses);
    }

    public List<Access> getLoggedOutAccesses() {
        return this.loggedOutAccesses;
    }

    public AccessResponseResult generateResponse(final AccessResponseRequest accessResponseRequest) {
        if (access != null) {
            return this.access.generateResponse(accessResponseRequest);
        }

        if (casServiceAccessRequest.isProxiedRequest()) {
            if (!casServiceAccessRequest.isValid()) {
                return DefaultAccessResponseResultImpl.generateErrorView("casProxyFailureView", "INVALID_REQUEST_PROXY", "INVALID_REQUEST_PROXY");
            }

            return DefaultAccessResponseResultImpl.generateErrorView("casProxyFailureView", "SERVER_ERROR", "SERVER_ERROR");
        }

        // the only other option is some form of passive authentication

        if (this.casServiceAccessRequest.isPassiveAuthentication()) {
            return DefaultAccessResponseResultImpl.generateRedirect(this.casServiceAccessRequest.getPassiveAuthenticationRedirectUrl());
        }

        throw new IllegalStateException("We don't know how to generate a response!");
    }
}
