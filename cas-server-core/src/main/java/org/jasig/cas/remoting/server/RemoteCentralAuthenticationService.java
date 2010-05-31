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

package org.jasig.cas.remoting.server;

import org.jasig.cas.server.CentralAuthenticationService;
import org.jasig.cas.server.authentication.Service;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.login.LoginRequest;
import org.jasig.cas.server.login.LoginResponse;
import org.jasig.cas.server.logout.LogoutRequest;
import org.jasig.cas.server.logout.LogoutResponse;
import org.jasig.cas.server.session.Assertion;
import org.jasig.cas.ticket.TicketException;
import org.springframework.util.Assert;

import javax.validation.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Wrapper implementation around a CentralAuthenticationService that does
 * completes the marshalling of parameters from the web-service layer to the
 * service layer. Typically the only thing that is done is to validate the
 * parameters (as you would in the web tier) and then delegate to the service
 * layer.
 * <p>
 * The following properties are required:
 * </p>
 * <ul>
 * <li>centralAuthenticationService - the service layer we are delegating to.</li>
 * </ul>
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class RemoteCentralAuthenticationService implements CentralAuthenticationService {

    /** The CORE to delegate to. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    /** The validators to check the Credentials. */
    @NotNull
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public LoginResponse login(final LoginRequest loginRequest) {
        return this.centralAuthenticationService.login(loginRequest);
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) {
        return this.centralAuthenticationService.logout(logoutRequest);
    }

    public String grantServiceTicket(final String ticketGrantingTicketId, final Service service) {
        return this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service);
    }

    /**
     * @throws IllegalArgumentException if given invalid credentials
     */
    public String grantServiceTicket(final String ticketGrantingTicketId, final Service service, final Credential credentials) {
        checkForErrors(credentials);

        return this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service, credentials);
    }

    public Assertion validateServiceTicket(final String serviceTicketId, final Service service) {
        return this.centralAuthenticationService.validateServiceTicket(serviceTicketId, service);
    }

     /**
     * @throws IllegalArgumentException if the credentials are invalid.
     */
    public String delegateTicketGrantingTicket(final String serviceTicketId, final Credential credentials) {
        checkForErrors(credentials);

        return this.centralAuthenticationService.delegateTicketGrantingTicket(serviceTicketId, credentials);
    }

    private void checkForErrors(final Credential credentials) {
        if (credentials == null) {
            return;
        }
        
        final Set<ConstraintViolation<Credential>> errors = this.validator.validate(credentials);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Error validating credentials: " + errors.toString());
        }
    }

    /**
     * Set the CentralAuthenticationService.
     * 
     * @param centralAuthenticationService The CentralAuthenticationService to
     * set.
     */
    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    /**
     * Set the list of validators.
     * 
     * @param validator The array of validators to use.
     */
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
}
