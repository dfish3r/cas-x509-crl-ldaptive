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

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the {@link org.jasig.cas.server.session.Session} interface for use with Memcached, Infinispan, etc.
 *
 * @author Scott Battaglia
 * @version $Revision: 21600 $ $Date: 2010-09-07 00:01:04 -0400 (Tue, 07 Sep 2010) $
 * @since 4.0
 */
public final class SerializableSessionImpl extends AbstractStaticSession implements Serializable {

    /**
     * Marked as transient since we reconstruct everything with {@link #reinitializeSessions()}.
     */
    private transient Session parentSession;

    private String id;

    private boolean invalidate = false;

    private final Map<String, Access> accesses = new ConcurrentHashMap<String,Access>();

    private final AttributePrincipal attributePrincipal;

    private final SimpleStateImpl state = new SimpleStateImpl();

    private final Set<Session> childSessions = new HashSet<Session>();

    private final SortedSet<Authentication> authentications = new TreeSet<Authentication>();

    public SerializableSessionImpl(final AuthenticationResponse authenticationResponse) {
        this(null, authenticationResponse);
    }

    protected SerializableSessionImpl(final Session parentSession, final AuthenticationResponse authenticationResponse) {
        this.attributePrincipal = authenticationResponse.getPrincipal();
        this.parentSession = parentSession;
        this.authentications.addAll(authenticationResponse.getAuthentications());
        updateId();

        for (final Authentication authentication : authenticationResponse.getAuthentications()) {
            if (authentication.isLongTermAuthentication()) {
                this.state.setLongTermSessionExists(true);
                break;
            }
        }
    }

    @Override
    protected State getState() {
        return this.state;
    }

    protected Set<Session> getChildSessions() {
        return this.childSessions;
    }

    protected Session getParentSession() {
        return this.parentSession;
    }

    protected boolean executeExpirationPolicy() {
        return getExpirationPolicy().isExpired(this.state);
    }

    protected boolean isInvalid() {
        return this.invalidate;
    }

    protected void updateId() {
        this.id = UUID.randomUUID().toString();
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

    protected void addAccess(final Access access) {
        this.accesses.put(access.getId(), access);
    }

    protected void updateState() {
        this.state.updateState();
    }

    @Override
    protected Session createDelegatedSessionInternal(AuthenticationResponse authenticationResponse) {
        final Session session = new SerializableSessionImpl(this, authenticationResponse);
        this.childSessions.add(session);
        return session;
    }

    public SortedSet<Authentication> getAuthentications() {
        return this.authentications;
    }

    public AttributePrincipal getPrincipal() {
        return this.attributePrincipal;
    }

    public void addAuthentication(final Authentication authentication) {
        if (authentication.isLongTermAuthentication()) {
            this.state.setLongTermSessionExists(true);
        }

        this.authentications.add(authentication);
    }

    protected void setParentSession(final Session session) {
        this.parentSession = session;

        for (final Session childSession : this.childSessions) {
            ((SerializableSessionImpl) childSession).setParentSession(this);
        }
    }

    /**
     * Fixes any serialization issues.  Should really only be called on the parent.
     */
    public void reinitializeSessions() {
        for (final Session childSession : this.childSessions) {
            ((SerializableSessionImpl) childSession).setParentSession(this);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializableSessionImpl that = (SerializableSessionImpl) o;

        if (invalidate != that.invalidate) return false;
        if (accesses != null ? !accesses.equals(that.accesses) : that.accesses != null) return false;
        if (attributePrincipal != null ? !attributePrincipal.equals(that.attributePrincipal) : that.attributePrincipal != null)
            return false;
        if (authentications != null ? !authentications.equals(that.authentications) : that.authentications != null)
            return false;
        if (childSessions != null ? !childSessions.equals(that.childSessions) : that.childSessions != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (parentSession != null ? !parentSession.equals(that.parentSession) : that.parentSession != null)
            return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = parentSession != null ? parentSession.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (invalidate ? 1 : 0);
        result = 31 * result + (accesses != null ? accesses.hashCode() : 0);
        result = 31 * result + (attributePrincipal != null ? attributePrincipal.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (childSessions != null ? childSessions.hashCode() : 0);
        result = 31 * result + (authentications != null ? authentications.hashCode() : 0);
        return result;
    }
}
