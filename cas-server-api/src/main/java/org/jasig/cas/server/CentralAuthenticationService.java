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

import org.jasig.cas.server.authentication.Service;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.login.LoginRequest;
import org.jasig.cas.server.login.LoginResponse;
import org.jasig.cas.server.logout.LogoutRequest;
import org.jasig.cas.server.logout.LogoutResponse;
import org.jasig.cas.server.session.Assertion;

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
     * Grant a ServiceTicket for a Service.
     * 
     * @param ticketGrantingTicketId Proof of prior authentication.
     * @param service The target service of the ServiceTicket.
     * @return the ServiceTicket for target Service.
     */
    String grantServiceTicket(String ticketGrantingTicketId, Service service);

    /**
     * Grant a ServiceTicket for a Service *if* the principal resolved from the
     * credentials matches the principal associated with the
     * TicketGrantingTicket.
     * 
     * @param ticketGrantingTicketId Proof of prior authentication.
     * @param service The target service of the ServiceTicket.
     * @param credentials the Credentials to present to receive the
     * ServiceTicket
     * @return the ServiceTicket for target Service.
     */
    String grantServiceTicket(final String ticketGrantingTicketId,
        final Service service, final Credential credentials);

    /**
     * Validate a ServiceTicket for a particular Service.
     * 
     * @param serviceTicketId Proof of prior authentication.
     * @param service Service wishing to validate a prior authentication.
     * @return ServiceTicket if valid for the service
     */
    Assertion validateServiceTicket(final String serviceTicketId,
        final Service service);

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
    String delegateTicketGrantingTicket(final String serviceTicketId,
        final Credential credentials);
}
