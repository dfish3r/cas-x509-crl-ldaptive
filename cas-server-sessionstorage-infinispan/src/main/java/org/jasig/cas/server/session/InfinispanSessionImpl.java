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
 * Designed to work with JBoss's Infinispan by moving some attributes to static variables and re-initializing others (i.e. parent sessions).
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class InfinispanSessionImpl extends AbstractStaticSession {

    private final State state = new SimpleStateImpl();

    private final Map<String,Access> accesses = new HashMap<String,Access>();

    private final AttributePrincipal attributePrincipal;

    private String id;

    private final SortedSet<Authentication> authentications;

    private final Set<Session> childSessions = new HashSet<Session>();

    private Session parentSession;

    private boolean invalid;

    public InfinispanSessionImpl(final Set<Authentication> authentications, final AttributePrincipal attributePrincipal) {
        this(null, authentications, attributePrincipal);
    }

    public InfinispanSessionImpl(final Session parentSession, final Set<Authentication> authentications, final AttributePrincipal attributePrincipal) {
        this.parentSession = parentSession;
        this.authentications = new TreeSet<Authentication>(authentications);
        this.attributePrincipal = attributePrincipal;
        updateId();
    }

    @Override
    protected boolean executeExpirationPolicy() {
        return getExpirationPolicy().isExpired(this.state);
    }

    @Override
    protected boolean isInvalid() {
        return this.invalid;
    }

    @Override
    protected Session getParentSession() {
        return this.parentSession;
    }

    protected void updateId() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    protected void addAccess(Access access) {
        this.accesses.put(access.getId(), access);
    }

    @Override
    protected Set<Session> getChildSessions() {
        return this.childSessions;
    }

    @Override
    protected void setInvalidFlag() {
        this.invalid = true;
    }

    @Override
    protected void updateState() {
        this.state.updateState();
    }

    @Override
    protected State getState() {
        return this.state;
    }

    @Override
    protected Session createDelegatedSessionInternal(final AuthenticationResponse authenticationResponse) {
        final Session session = new InfinispanSessionImpl(this, authenticationResponse.getAuthentications(), authenticationResponse.getPrincipal());
        this.childSessions.add(session);
        return session;
    }

    public String getId() {
        return this.id;
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

    public Access getAccess(String accessId) {
        return this.accesses.get(accessId);
    }

    public Collection<Access> getAccesses() {
        return this.accesses.values();
    }

    protected void setParentSession(final Session session) {
        this.parentSession = session;

        for (final Session childSession : this.childSessions) {
            ((InfinispanSessionImpl) childSession).setParentSession(this);
        }
    }

    /**
     * Fixes any serialization issues.  Should really only be called on the parent.
     */
    public void reinitializeSessions() {
        for (final Session childSession : this.childSessions) {
            ((InfinispanSessionImpl) childSession).setParentSession(this);
        }
    }
}
