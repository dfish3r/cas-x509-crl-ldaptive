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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default in-memory implementation of the Sessions.  Since objects are stored in local memory only references
 * to factories, etc. can be maintained within the application.  Sessions that might be stored in databases, etc, would
 * not necessarily preserve the links during storage, but may instead re-establish the link once the object is
 * retrieved from its data store.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class InMemorySessionImpl extends AbstractSession {

    private final Session parentSession;

    private final ExpirationPolicy expirationPolicy;

    private String id;

    private boolean invalidate = false;

    private final Map<String, Access> accesses = new ConcurrentHashMap<String,Access>();

    private final List<AccessFactory> accessFactories;

    private SortedSet<Authentication> authentications;

    private final State state = new SimpleStateImpl();

    private final Set<Session> childSessions = new HashSet<Session>();

    private final AttributePrincipal attributePrincipal;

    private final ServicesManager servicesManager;

    public InMemorySessionImpl(final ExpirationPolicy expirationPolicy, final List<AccessFactory> accessFactories, final Set<Authentication> authentications, final AttributePrincipal attributePrincipal, final ServicesManager servicesManager) {
        this(null, expirationPolicy, accessFactories, authentications, attributePrincipal, servicesManager);
    }

    public InMemorySessionImpl(final Session parentSession, final ExpirationPolicy expirationPolicy, final List<AccessFactory> accessFactories, final Set<Authentication> authentications, final AttributePrincipal attributePrincipal, final ServicesManager servicesManager) {
        this.parentSession = parentSession;
        this.expirationPolicy = expirationPolicy;
        this.accessFactories = accessFactories;
        this.authentications = new TreeSet<Authentication>(authentications);
        this.attributePrincipal = attributePrincipal;
        this.servicesManager = servicesManager;
        updateId();
    }

     public SortedSet<Authentication> getAuthentications() {
        return this.authentications;
    }

    public AttributePrincipal getPrincipal() {
        return this.attributePrincipal;
    }

    public void addAuthentication(final Authentication authentication) {
        this.authentications.add(authentication);
    }

    protected Set<Session> getChildSessions() {
        return this.childSessions;
    }

    protected Session getParentSession() {
        return this.parentSession;
    }

    protected boolean executeExpirationPolicy() {
        return this.expirationPolicy.isExpired(state);
    }

    protected boolean isInvalid() {
        return this.invalidate;
    }

    protected void updateId() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    protected State getState() {
        return this.state;
    }

    @Override
    protected ServicesManager getServicesManager() {
        return this.servicesManager;
    }

    public String getId() {
        return this.id;
    }

    protected void setInvalidFlag() {
        this.invalidate = true;
    }

    public Access getAccess(String accessId) {
        return this.accesses.get(accessId);
    }

    public Collection<Access> getAccesses() {
        return this.accesses.values();
    }

    protected List<AccessFactory> getAccessFactories() {
        return this.accessFactories;
    }

    protected void addAccess(final Access access) {
        this.accesses.put(access.getId(), access);
    }

    protected void updateState() {
        this.state.updateState();
    }

    protected Session createDelegatedSessionInternal(final AuthenticationResponse authenticationResponse) {
        final Session session = new InMemorySessionImpl(this, this.expirationPolicy, this.accessFactories, authenticationResponse.getAuthentications(), authenticationResponse.getPrincipal(), getServicesManager());
        this.childSessions.add(session);
        return session;
    }
}
