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

/**
 * BerkeleyDB-backed version of the Session interface.
 *
 * @version $Revision$ $Date$
 * @since 3.5.0
 */
public final class BerkeleyDbSessionImpl extends AbstractSession {

    private static ExpirationPolicy EXPIRATION_POLICY;

    private static List<AccessFactory> ACCESS_FACTORIES;

    private final Map<String, Access> accesses = new HashMap<String, Access>();

    private final Set<Authentication> authentications = new HashSet<Authentication>();

    private final AttributePrincipal attributePrincipal;

    private final State state = new SimpleStateImpl();

    private final Session parentSession;

    private final Set<Session> childSessions = new HashSet<Session>();

    private boolean invalidate = false;

    private String id;

    public BerkeleyDbSessionImpl(final Set<Authentication> authentications, final AttributePrincipal attributePrincipal) {
        this(null, authentications, attributePrincipal);
    }

    public BerkeleyDbSessionImpl(final Session parentSession, final Set<Authentication> authentications, final AttributePrincipal attributePrincipal) {
        this.parentSession = parentSession;
        this.authentications.addAll(authentications);
        this.attributePrincipal = attributePrincipal;
        updateId();
    }

    public static void setExpirationPolicy(final ExpirationPolicy expirationPolicy) {
        EXPIRATION_POLICY = expirationPolicy;
    }

    public static void setAccessFactories(final List<AccessFactory> accessFactories) {
        ACCESS_FACTORIES = accessFactories;
    }

    @Override
    protected boolean executeExpirationPolicy() {
        return EXPIRATION_POLICY.isExpired(this.state);
    }

    @Override
    protected boolean isInvalid() {
        return this.invalidate;
    }

    @Override
    protected Session getParentSession() {
        return this.parentSession;
    }

    @Override
    protected void updateId() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    protected List<AccessFactory> getAccessFactories() {
        return ACCESS_FACTORIES;
    }

    @Override
    protected void addAccess(final Access access) {
        this.accesses.put(access.getId(), access);
    }

    @Override
    protected Set<Session> getChildSessions() {
        return this.childSessions;
    }

    @Override
    protected void setInvalidFlag() {
        this.invalidate = true;
    }

    @Override
    protected void updateState() {
        this.state.updateState();
    }

    @Override
    protected Session createDelegatedSessionInternal(final AuthenticationResponse authenticationResponse) {
        final Session session = new BerkeleyDbSessionImpl(this, authenticationResponse.getAuthentications(), authenticationResponse.getPrincipal());
        this.childSessions.add(session);
        return session;
    }

    @Override
    public boolean hasNotBeenUsed() {
        return this.accesses.isEmpty();
    }

    public String getId() {
        return this.id;
    }

    public Set<Authentication> getAuthentications() {
        return this.authentications;
    }

    public AttributePrincipal getPrincipal() {
        return this.attributePrincipal;
    }

    public void addAuthentication(final Authentication authentication) {
        this.authentications.add(authentication);
    }

    public Access getAccess(final String accessId) {
        return this.accesses.get(accessId);
    }

    public Collection<Access> getAccesses() {
        return this.accesses.values();
    }
}
