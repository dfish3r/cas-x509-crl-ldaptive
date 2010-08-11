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

import org.jasig.cas.server.authentication.AttributePrincipal;
import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements the underlying domain object methods that should be common among every specific implementation of the
 * session interface.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSession implements Session {

    protected final static Logger log = LoggerFactory.getLogger(AbstractSession.class);

    /**
     * Retrieve the expiration policy, which defines whether this session is still valid or not.
     *
     * @return the result of {@link org.jasig.cas.server.session.ExpirationPolicy#isExpired(State)}
     */
    protected abstract boolean executeExpirationPolicy();

    /**
     * Determines whether this session has been marked as invalid or not (regardless of expiration policy)
     *
     * @return true if the session has been marked as invalid, false otherwise.
     */

    protected abstract boolean isInvalid();

    /**
     * Returns the parent session to this session, if they are linked.  CAN be null.
     *
     * @return the parent session or null if there is none.
     */
    protected abstract Session getParentSession();

    /**
     * Called after certain actions to regenerate the identifier for this session.  Things that store links to the session
     * should be aware that the identifier can change.  Note, however, that updating identifiers is NOT required.
     */
    protected abstract void updateId();

    /**
     * Retrieves the list of AccessFactories.
     *
     * @return the list of access factories.  CANNOT be null.  SHOULD NOT be empty.
     */
    protected abstract List<AccessFactory> getAccessFactories();

    /**
     * Store the Access created in the meta data for the
     *
     * @param access the access to store.
     */
    protected abstract void addAccess(Access access);

    /**
     * Retrieves the child sessions.
     *
     * @return the child sessions.  CANNOT be NULL.  Can be EMPTY.
     */
    protected abstract Set<Session> getChildSessions();

    /**
     * Sets the invalid flag on a subclass.
     */
    protected abstract void setInvalidFlag();

    /**
     * Updates the internal state of a subclass.
     */
    protected abstract void updateState();

    /**
     * Executes after the default actions must happen when createDelegatedSession is called.
     *
     * @param authenticationResponse the authentication response
     * @return the delegated session.
     */
    protected abstract Session createDelegatedSessionInternal(final AuthenticationResponse authenticationResponse);

    /**
     * Returns an instance of the ServicesManager tool.  CANNOT be NULL.
     *
     * @return the services manager tool.
     */
    protected abstract ServicesManager getServicesManager();

    public synchronized final Set<Access> invalidate() {
        final Set<Access> accesses = new HashSet<Access>();
        accesses.addAll(getAccesses());

        for (final Access access : getAccesses()) {
            access.invalidate();
        }

        for (final Session session : getChildSessions()) {
            accesses.addAll(session.invalidate());
        }

        setInvalidFlag();
        return accesses;
    }

    public final boolean isValid() {
        return !isInvalid() && !executeExpirationPolicy() && (getParentSession() == null || getParentSession().isValid());
    }

    public final AttributePrincipal getRootPrincipal() {
        if (isRoot()) {
            return getPrincipal();
        }

        return getParentSession().getRootPrincipal();
    }

    public final Set<Authentication> getRootAuthentications() {
        if (isRoot()) {
            return getAuthentications();
        }

        return getParentSession().getRootAuthentications();
    }

    public final Session getRootSession() {
        if (getParentSession() != null) {
            return getParentSession().getRootSession();
        }

        return this;
    }

    public synchronized final Access grant(final ServiceAccessRequest serviceAccessRequest) throws InvalidatedSessionException {
        if (!isValid()) {
            throw new InvalidatedSessionException("Session is no longer valid.");
        }

        updateState();

        for (final AccessFactory accessFactory : getAccessFactories()) {
            final Access access = accessFactory.getAccess(this, serviceAccessRequest);

            if (access != null) {
                final RegisteredService registeredService = getServicesManager().findServiceBy(access);

                if (registeredService == null || !registeredService.isEnabled()) {
                    log.warn("ServiceManagement: Unauthorized Service Access. Service [" + access.getResourceIdentifier() + "] not found in Service Registry.");
                    throw new UnauthorizedServiceException();
                }

                if (!registeredService.isSsoEnabled() && serviceAccessRequest.getCredentials().isEmpty() && !this.hasNotBeenUsed()) {
                    log.warn("ServiceManagement: Service Not Allowed to use SSO.  Service [" + access.getResourceIdentifier() + "]");
                    throw new UnauthorizedSsoServiceException();
                }

                if (access.requiresStorage()) {
                    addAccess(access);
                }
                // TODO re-enable later when we want to change session ids
                // updateId();
                return access;
            }
        }

        throw new IllegalStateException("No AccessFactories configured that can execute Access request.");
    }

    public final List<AttributePrincipal> getProxiedPrincipals() {
        if (getParentSession() == null) {
            return new ArrayList<AttributePrincipal>();
        }

        final List<AttributePrincipal> principals = getParentSession().getProxiedPrincipals();
        principals.add(getPrincipal());
        return principals;
    }

    public final List<Set<Authentication>> getProxiedAuthentications() {
        if (getParentSession() == null) {
            return new ArrayList<Set<Authentication>>();
        }

        final List<Set<Authentication>> authentications = getParentSession().getProxiedAuthentications();
        authentications.add(getAuthentications());
        return authentications;
    }

    public final boolean isRoot() {
        return getParentSession() == null;
    }

    public final Session findChildSessionById(final String identifier) {
        for (final Session session : getChildSessions()) {
            if (session.getId().equals(identifier)) {
                return session;
            }
        }
        return null;
    }

    public synchronized final Session createDelegatedSession(final Access access, final AuthenticationResponse authenticationResponse) {
        updateState();

        final RegisteredService registeredService = getServicesManager().findServiceBy(access);

        if (registeredService == null || !registeredService.isEnabled()
            || !registeredService.isAllowedToProxy()) {
            log.warn("ServiceManagement: Service Attempted to Proxy, but is not allowed.  Service: [" + access.getResourceIdentifier() + "]");
            throw new UnauthorizedProxyingException();
        }
        
        return createDelegatedSessionInternal(authenticationResponse);
    }

    public final boolean hasNotBeenUsed() {
        return getAccesses().isEmpty();
    }
}
