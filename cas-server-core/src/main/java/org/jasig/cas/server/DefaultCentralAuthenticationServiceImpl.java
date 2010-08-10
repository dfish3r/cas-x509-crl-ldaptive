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

import com.github.inspektr.audit.annotation.Audit;
import org.jasig.cas.server.authentication.*;
import org.jasig.cas.server.login.*;
import org.jasig.cas.server.logout.DefaultLogoutResponseImpl;
import org.jasig.cas.server.logout.LogoutRequest;
import org.jasig.cas.server.logout.LogoutResponse;
import org.jasig.cas.server.session.*;
import org.jasig.cas.server.session.ServicesManager;
import org.jasig.cas.server.session.RegisteredService;
import org.jasig.cas.server.session.UnauthorizedProxyingException;
import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Concrete implementation of a CentralAuthenticationService, and also the
 * central, organizing component of CAS's internal implementation.
 * <p>
 * This class is thread safe.
 * <p>
 * This class has the following properties that must be set:
 * <ul>
 * <li> <code>ticketRegistry</code> - The Ticket Registry to maintain the list
 * of available tickets.</li>
 * <li> <code>serviceTicketRegistry</code> - Provides an alternative to configure separate registries for TGTs and ST in order to store them
 * in different locations (i.e. long term memory or short-term)</li>
 * <li> <code>authenticationManager</code> - The service that will handle
 * authentication.</li>
 * <li> <code>ticketGrantingTicketUniqueTicketIdGenerator</code> - Plug in to
 * generate unique secure ids for TicketGrantingTickets.</li>
 * <li> <code>serviceTicketUniqueTicketIdGenerator</code> - Plug in to
 * generate unique secure ids for ServiceTickets.</li>
 * <li> <code>ticketGrantingTicketExpirationPolicy</code> - The expiration
 * policy for TicketGrantingTickets.</li>
 * <li> <code>serviceTicketExpirationPolicy</code> - The expiration policy for
 * ServiceTickets.</li>
 * </ul>
 * 
 * @author William G. Thompson, Jr.
 * @author Scott Battaglia
 * @author Dmitry Kopylenko
 * @version $Revision: 1.16 $ $Date: 2007/04/24 18:11:36 $
 * @since 3.0
 */
public final class DefaultCentralAuthenticationServiceImpl implements CentralAuthenticationService {

    /** Log instance for logging events, info, warnings, errors, etc. */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * AuthenticationManager for authenticating credentials for purposes of
     * obtaining tickets.
     */
    @NotNull
    private final AuthenticationManager authenticationManager;

    @NotNull
    private final SessionStorage sessionStorage;

    @NotNull
    private List<PreAuthenticationPlugin> preAuthenticationPlugins = new ArrayList<PreAuthenticationPlugin>();

    @NotNull
    private List<AuthenticationResponsePlugin> authenticationResponsePlugins = new ArrayList<AuthenticationResponsePlugin>();

    public DefaultCentralAuthenticationServiceImpl(final AuthenticationManager authenticationManager, final SessionStorage sessionStorage) {
        this.authenticationManager = authenticationManager;
        this.sessionStorage = sessionStorage;
    }

    @Audit(action="CREATE_SESSION", actionResolverName="CREATE_SESSION_RESOLVER", resourceResolverName="CREATE_SESSION_RESOURCE_RESOLVER")
    @Profiled(tag = "CREATE_SESSION", logFailuresSeparately = false)
    public LoginResponse login(final LoginRequest loginRequest) {
        Assert.notNull(loginRequest, "loginRequest cannot be null.");
        final AuthenticationRequest authenticationRequest = new DefaultAuthenticationRequestImpl(loginRequest.getCredentials(), loginRequest.isLongTermLoginRequest());

        for (final PreAuthenticationPlugin plugin : this.preAuthenticationPlugins) {
            final LoginResponse loginResponse = plugin.continueWithAuthentication(loginRequest);

            if (loginResponse != null) {
                return loginResponse;
            }
        }

        final AuthenticationResponse authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        for (final AuthenticationResponsePlugin authenticationResponsePlugin : this.authenticationResponsePlugins) {
            authenticationResponsePlugin.handle(loginRequest, authenticationResponse);
        }

        if (authenticationResponse.succeeded()) {
            final Session session = this.sessionStorage.createSession(authenticationResponse);
            return new DefaultLoginResponseImpl(session, authenticationResponse);
        }

        return new DefaultLoginResponseImpl(authenticationResponse);
    }


    /**
     * Note, we only currently support this is on the top, user-initiated session.
     */
    @Audit(action="DESTROY_SESSION",actionResolverName="DESTROY_SESSION_RESOLVER",resourceResolverName="DESTROY_SESSION_RESOURCE_RESOLVER")
    @Profiled(tag = "DESTROY_SESSION",logFailuresSeparately = false)
    public LogoutResponse logout(final LogoutRequest logoutRequest) {
        final Session session = this.sessionStorage.destroySession(logoutRequest.getSessionId());

        if (session != null) {
            session.invalidate();
            return new DefaultLogoutResponseImpl(session);
        }

        return new DefaultLogoutResponseImpl();
    }

    @Audit(action="ADMIN_DESTROY_SESSIONS",actionResolverName="ADMIN_DESTROY_SESSIONS_RESOLVER",resourceResolverName="ADMIN_DESTROY_SESSIONS_RESOURCE_RESOLVER")
    @Profiled(tag = "ADMIN_DESTROY_SESSION",logFailuresSeparately = false)
    public LogoutResponse logout(final String userId) {
        Assert.notNull(userId, "userId cannot be null");
        final Set<Session> sessions = this.sessionStorage.findSessionsByPrincipal(userId);

        if (sessions.isEmpty()) {
            return new DefaultLogoutResponseImpl();
        }

        final Set<Session> destroyedSessions = new HashSet<Session>();

        for (final Session session : sessions) {
            final Session destroyedSession = this.sessionStorage.destroySession(session.getId());
            destroyedSessions.add(destroyedSession);
        }

        return new DefaultLogoutResponseImpl(destroyedSessions);
    }

    @Audit(action="VALIDATE_ACCESS",actionResolverName="VALIDATE_ACCESS_RESOLVER",resourceResolverName="VALIDATE_ACCESS_RESOURCE_RESOLVER")
    @Profiled(tag="VALIDATE_ACCESS",logFailuresSeparately = false)
    public Access validate(final TokenServiceAccessRequest tokenServiceAccessRequest) {
        Assert.notNull(tokenServiceAccessRequest, "tokenServiceAccessRequest cannot be null");

        final Session session = this.sessionStorage.findSessionByAccessId(tokenServiceAccessRequest.getToken());

        if (session == null) {
            return null;
        }

        final Access access = session.getAccess(tokenServiceAccessRequest.getToken());

        if (access == null) {
            return null;
        }
        access.validate(tokenServiceAccessRequest);
        return access;
    }

    @Audit(action="ACCESS",actionResolverName="GRANT_ACCESS_RESOLVER",resourceResolverName="GRANT_ACCESS_RESOURCE_RESOLVER")
    @Profiled(tag="GRANT_ACCESS", logFailuresSeparately = false)
    public ServiceAccessResponse grantAccess(final ServiceAccessRequest serviceAccessRequest) throws SessionException, AccessException {
        Assert.notNull(serviceAccessRequest, "serviceAccessRequest cannot be null.");

        final Session session = this.sessionStorage.findSessionBySessionId(serviceAccessRequest.getSessionId());

        if (session == null) {
            throw new NotFoundSessionException(String.format("Session [%s] could not be found.", serviceAccessRequest.getSessionId()));
        }

        if (!session.isValid()) {
            throw new InvalidatedSessionException(String.format("Session [%s] is no longer valid.", session.getId()));
        }

        final Session sessionToWorkWith;
        final List<Access> remainingAccesses = new ArrayList<Access>();
        final AuthenticationResponse authenticationResponse;
        if (serviceAccessRequest.isForceAuthentication()) {
            // TODO we need to do all the steps, including the pre-auth ones above under login.
            final AuthenticationRequest authenticationRequest = new DefaultAuthenticationRequestImpl(serviceAccessRequest.getCredentials(), serviceAccessRequest.isLongTermLoginRequest());
            authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

            if (!authenticationResponse.succeeded()) {
                return new DefaultServiceAccessResponseImpl(null, Collections.<Access>emptyList(), authenticationResponse);
            }

            if (!authenticationResponse.getPrincipal().equals(session.getPrincipal())) {
                // expire the existing session and get a new session
                final Session destroyedSession = this.sessionStorage.destroySession(session.getId());
                destroyedSession.invalidate();
                final LogoutResponse logoutResponse = new DefaultLogoutResponseImpl(destroyedSession);
                remainingAccesses.addAll(logoutResponse.getLoggedInAccesses());
                sessionToWorkWith = this.sessionStorage.createSession(authenticationResponse);

            } else {
                session.getAuthentications().addAll(authenticationResponse.getAuthentications());
                sessionToWorkWith = session;
            }
        } else {
            authenticationResponse = null;
            sessionToWorkWith = session;
        }

        final Access access = sessionToWorkWith.grant(serviceAccessRequest);
        this.sessionStorage.updateSession(sessionToWorkWith);

        return new DefaultServiceAccessResponseImpl(access, remainingAccesses, sessionToWorkWith, authenticationResponse);
    }

    /**
     * @throws IllegalArgumentException if the ServiceTicketId or the
     * Credentials are null.
     */
    @Audit(action="PROXY_GRANTING_TICKET",actionResolverName="GRANT_PROXY_GRANTING_TICKET_RESOLVER",resourceResolverName="GRANT_PROXY_GRANTING_TICKET_RESOURCE_RESOLVER")
    @Profiled(tag="GRANT_PROXY_GRANTING_TICKET",logFailuresSeparately = false)
    @Transactional(readOnly = false)
    public String delegateTicketGrantingTicket(final String serviceTicketId, final Credential credential) {

        Assert.notNull(serviceTicketId, "serviceTicketId cannot be null");
        Assert.notNull(credential, "credentials cannot be null");

        final AuthenticationRequest authenticationRequest = new DefaultAuthenticationRequestImpl(Arrays.asList(credential), false);
        final AuthenticationResponse authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        final Session session = this.sessionStorage.findSessionByAccessId(serviceTicketId);

        if (session == null) {
            throw new IllegalStateException();
        }

        final Access access = session.getAccess(serviceTicketId);

        // TODO we should be doing more a check than this.  Not sure why I didn't have that on the interface
        if (access == null) {
            throw new IllegalStateException();
        }

        try {
            final Session delegatedSession = session.createDelegatedSession(access, authenticationResponse);
            // TODO not sure if this will work
            this.sessionStorage.updateSession(session);
            return this.sessionStorage.updateSession(delegatedSession).getId();

        } catch (final InvalidatedSessionException e) {
            throw new IllegalStateException(e);
        }
    }

    public void setPreAuthenticationPlugins(final List<PreAuthenticationPlugin> preAuthenticationPlugins) {
        this.preAuthenticationPlugins = preAuthenticationPlugins;
    }

    public void setAuthenticationResponsePlugins(final List<AuthenticationResponsePlugin> authenticationResponsePlugins) {
        this.authenticationResponsePlugins = authenticationResponsePlugins;
    }
}
