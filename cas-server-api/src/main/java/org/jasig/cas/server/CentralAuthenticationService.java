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

package org.jasig.cas.server;

import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.login.*;
import org.jasig.cas.server.logout.LogoutRequest;
import org.jasig.cas.server.logout.LogoutResponse;
import org.jasig.cas.server.session.Access;
import org.jasig.cas.server.session.AccessException;
import org.jasig.cas.server.session.SessionException;

/**
 * CAS viewed as a set of services to generate and validate Tickets.
 * <p>
 * This is the interface between a Web HTML, Web Services, RMI, or any other
 * request processing layer and the CAS Service viewed as a mechanism to
 * generate, store, validate, and retrieve Tickets containing Authentication
 * information. The features of the request processing layer (the HttpXXX
 * Servlet objects) are not visible here or in any modules behind this layer. In
 * theory, a standalone application could call these methods directly as a
 * private authentication service.
 * </p>
 * 
 * @author William G. Thompson, Jr.
 * @author Dmitry Kopylenko
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public interface CentralAuthenticationService {

    /**
     * Initiate a single sign on session with the service by providing credentials
     * to the system along with a request for authentication.
     *
     * @param loginRequest the request for authentication
     * @return the response to the authentication request.
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * Log out of any existing session you may have with the system.
     *
     * @param logoutRequest the request to log out of the system.
     * @return the response from the server about the logout request.
     */
    LogoutResponse logout(LogoutRequest logoutRequest);

    /**
     * Special function to administratively log out a user.  This should not be exposed by the web tier to
     * normal users.  It is an administrative function only. I.e. an administration tool or JMX.
     *
     * @param userId the user name to destroy all sessions for.  CANNOT be NULL.
     * @return the response to the logout attempt. 
     */
    LogoutResponse logout(String userId);

    /**
     * Method to request access to a particular service, or resource.
     *
     * @param serviceAccessRequest the actual request.  CAN only be NULL if the new authentication attempt has failed.
     * @return the response to the request for access.  This is only returned for a successful access.
     * @throws SessionException when there is a problem with the actual session.
     * @throws AccessException when there is a problem granting an access request.
     */
    ServiceAccessResponse grantAccess(ServiceAccessRequest serviceAccessRequest) throws SessionException, AccessException;

    /**
     * Validate requests that cannot be self-validating (i.e. CAS tickets)
     *
     * @param tokenServiceAccessRequest the request to validate
     * @return the response from the validation request.  CANNOT be NULL.
     */

    Access validate(TokenServiceAccessRequest tokenServiceAccessRequest);

    /**
     * Delegate a TicketGrantingTicket to a Service for proxying authentication
     * to other Services.
     * 
     * @param serviceTicketId The service ticket that will delegate to a
     * TicketGrantingTicket
     * @param credentials The credentials of the service that wishes to have a
     * TicketGrantingTicket delegated to it.
     * @return TicketGrantingTicket that can grant ServiceTickets that proxy
     * authentication.
     */
    String delegateTicketGrantingTicket(final String serviceTicketId, final Credential credentials);
}
