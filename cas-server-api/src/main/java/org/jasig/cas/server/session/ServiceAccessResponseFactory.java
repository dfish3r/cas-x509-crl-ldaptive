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

import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.jasig.cas.server.login.ServiceAccessResponse;

import java.util.List;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public interface ServiceAccessResponseFactory {

    /**
     * Generate a {@link ServiceAccessResponse} for a particular {@link ServiceAccessRequest}.  Typically these requests
     * have had {@link org.jasig.cas.server.login.ServiceAccessRequest#isValid()} return false.
     *
     * @param serviceAccessRequest the access request for which we're generating a service response.  CANNOT be NULL.
     *
     * @return the {@link ServiceAccessResponse}.  CANNOT be NULL.
     */
    ServiceAccessResponse getServiceAccessResponse(ServiceAccessRequest serviceAccessRequest);

    /**
     * Generate a {@link ServiceAccessResponse} for a particular {@link ServiceAccessRequest}.  Typically these requests
     * have had {@link org.jasig.cas.server.login.ServiceAccessRequest#isValid()} return false.
     *
     * @param serviceAccessRequest the access request for which we're generating a service response.  CANNOT be NULL.
     * @param authenticationResponse the authentication response if we attempted to re-authenticate.  CANNOT be NULL.
     *
     * @return the {@link ServiceAccessResponse}.  CANNOT be NULL.
     */
    ServiceAccessResponse getServiceAccessResponse(ServiceAccessRequest serviceAccessRequest, AuthenticationResponse authenticationResponse);

    /**
     * Generate an appropriate response based on a successful service access attempt.
     *
     * @param session the session that was used to grant the access. CANNOT be NULL.
     * @param access the access that was granted.  CANNOT be NULL.
     * @param authenticationResponse the related authentication if there was some form of re-authentication. CAN be NULL.
     * @param loggedOutAccesses if we invalidated some session and there were still sessions to log out. CAN be NULL.
     *
     * @return the {@link ServiceAccessResponse}.  CANNOT be NULL.
     */
    ServiceAccessResponse getServiceAccessResponse(Session session, Access access, AuthenticationResponse authenticationResponse, List<Access> loggedOutAccesses);

    /**
     * Returns true if the factory can handle this serviceAccessRequest.  You should be sure this is true before you
     * attempt to get a response.
     * <p>
     * If there is no access, we should always fall back to this method to determine support.
     *
     * @param serviceAccessRequest check if it can support this.
     * @return true if it can, false otherwise.
     */
    boolean supports(ServiceAccessRequest serviceAccessRequest);

    /**
     * Returns true if the factory can handle this access.  You should be sure this is true before you attempt to get a
     * response.
     *
     * @param access check if it can support this.
     * @return true if it can, false otherwise.
     */
    boolean supports(Access access);
}
