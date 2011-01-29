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
import org.jasig.cas.server.session.AbstractCasProtocolAccessImpl;
import org.jasig.cas.server.session.Access;
import org.jasig.cas.server.session.ServiceAccessResponseFactory;
import org.jasig.cas.server.session.Session;

import java.util.List;

/**
 * Returns instances of {@link CasServiceAccessResponseImpl}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class CasServiceAccessResponseImplFactory implements ServiceAccessResponseFactory {

    public ServiceAccessResponse getServiceAccessResponse(final ServiceAccessRequest serviceAccessRequest) {
        return new CasServiceAccessResponseImpl(serviceAccessRequest);
    }

    public ServiceAccessResponse getServiceAccessResponse(final ServiceAccessRequest serviceAccessRequest, final AuthenticationResponse authenticationResponse) {
        return new CasServiceAccessResponseImpl(serviceAccessRequest, authenticationResponse);
    }

    public ServiceAccessResponse getServiceAccessResponse(final Session session, final Access access, final AuthenticationResponse authenticationResponse, final List<Access> loggedOutAccesses) {
        return new CasServiceAccessResponseImpl(session, access, authenticationResponse, loggedOutAccesses);
    }

    public boolean supports(ServiceAccessRequest serviceAccessRequest) {
        return serviceAccessRequest instanceof CasServiceAccessRequestImpl;
    }

    public boolean supports(Access access) {
        return access instanceof AbstractCasProtocolAccessImpl;
    }
}
