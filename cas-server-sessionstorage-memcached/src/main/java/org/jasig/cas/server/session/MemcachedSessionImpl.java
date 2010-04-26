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

import org.jasig.cas.server.authentication.Authentication;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the {@link org.jasig.cas.server.session.Session} interface for use with Memcached.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class MemcachedSessionImpl extends AbstractStaticSession implements Serializable {

    // TODO will this actually work?
    private final Session parentSession;

    private String id;

    private boolean invalidate = false;

    private final Map<String, Access> accesses = new ConcurrentHashMap<String,Access>();

    private Authentication authentication;

    private final State state = new SimpleStateImpl();

    private final Set<Session> childSessions = new HashSet<Session>();

    public MemcachedSessionImpl(final Authentication authentication) {
        this(null, authentication);
    }
    
    protected MemcachedSessionImpl(final Session parentSession, final Authentication authentication) {
        this.parentSession = parentSession;
        this.authentication = authentication;
        updateId();
    }

    protected Set<Session> getChildSessions() {
        return this.childSessions;
    }

    protected Session getParentSession() {
        return this.parentSession;
    }

    protected boolean executeExpirationPolicy() {
        return getExpirationPolicy().isExpired(state, getRootAuthentication());
    }

    protected boolean isInvalid() {
        return this.invalidate;
    }

    protected void updateId() {
        // doesn't do anything
    }

    public String getId() {
        return this.id;
    }

    public void updateAuthentication(final Authentication authentication) {
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return this.authentication;
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

    protected Session createDelegatedSessionInternal(final Authentication authentication) {
        final Session session = new MemcachedSessionImpl(this, authentication);
        this.childSessions.add(session);
        return session;
    }
}
