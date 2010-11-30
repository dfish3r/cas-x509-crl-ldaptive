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

import org.infinispan.Cache;
import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.springframework.util.Assert;

import java.util.*;

/**
 * <p>Note: this class does not implement {@link org.jasig.cas.server.util.Cleanable}.  It depends on the internal
 * mechanisms for pruning, which <strong>MAY</strong> clash with your expiration policy settings. See the JBoss Infinispan
 * <a href="http://community.jboss.org/wiki/Eviction#Configuring_eviction">eviction documentation</a> for more details.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */

// TODO attach to eviction thread to
public final class InfinispanSessionStorageImpl extends AbstractSessionStorage {

    private final Cache<String,InfinispanSessionImpl> cache;

    private final Cache<String,String> cacheMappings;

    private final Cache<String, List<String>> principalMappings;

    public InfinispanSessionStorageImpl(final Cache<String,InfinispanSessionImpl> cache, final Cache<String,String> cacheMappings, final Cache<String,List<String>> principalMappings, final List<AccessFactory> accessFactories, final ServicesManager servicesManager) {
        super(accessFactories, servicesManager);
        this.cache = cache;
        this.cacheMappings = cacheMappings;
        this.principalMappings = principalMappings;
    }

    public Session createSession(final AuthenticationResponse authenticationResponse) {
        final InfinispanSessionImpl session = new InfinispanSessionImpl(authenticationResponse.getAuthentications(), authenticationResponse.getPrincipal());
        this.cache.put(session.getId(), session);
        this.cacheMappings.put(session.getId(), session.getId());

        final List<String> sessions = new ArrayList<String>();

        final List<String> existingSessions = this.principalMappings.get(session.getPrincipal().getName());

        if (existingSessions != null) {
            sessions.addAll(existingSessions);
        }

        sessions.add(session.getId());
        this.principalMappings.put(session.getPrincipal().getName(), sessions);

        return session;
    }

    public Session destroySession(final String sessionId) {
        Assert.notNull(sessionId, "sessionId cannot be null.");
        // find the session
        final Session session = findSessionBySessionId(sessionId);

        if (session == null) {
            return null;
        }

        // remove its mappings
        this.cacheMappings.remove(sessionId);

        if (session.isRoot()) {
            final List<String> sessions = this.principalMappings.get(session.getPrincipal().getName());

            // remove any listing in the principal map
            if (sessions != null) {
                sessions.remove(sessionId);
                this.principalMappings.put(session.getPrincipal().getName(), sessions);
            }

            // remove it
            this.cache.remove(sessionId);
        }

        return session;
    }

    public Session findSessionBySessionId(final String sessionId) {
        Assert.notNull(sessionId, "sessionId cannot be null.");
        final String actualSession = this.cacheMappings.get(sessionId);

        if (actualSession == null) {
            return null;
        }

        final InfinispanSessionImpl session = this.cache.get(actualSession);

        if (session == null) {
            return null;
        }

        session.reinitializeSessions();
        return session;
    }

    public Session updateSession(final Session session) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Session findSessionByAccessId(final String accessId) {
        Assert.notNull(accessId, "accessId cannot be null.");
        final String lowestLevelSession = this.cacheMappings.get(accessId);

        if (lowestLevelSession == null) {
            return null;
        }

        final String actualSession = this.cacheMappings.get(lowestLevelSession);

        if (actualSession == null) {
            return null;
        }

        final InfinispanSessionImpl session = this.cache.get(actualSession);

        if (session == null) {
            return null;
        }

        session.reinitializeSessions();

        if (actualSession.equals(session.getId())) {

            return session;
        }

        return session.findChildSessionById(lowestLevelSession);
    }

    public Set<Session> findSessionsByPrincipal(final String principalName) {
        Assert.notNull(principalName, "principalName cannot be null.");
        final List<String> sessionStrings = this.principalMappings.get(principalName);

        if (sessionStrings == null || sessionStrings.isEmpty()) {
            return Collections.emptySet();
        }

        final Set<Session> sessions = new HashSet<Session>();

        for (final String id : sessionStrings) {
            final Session session = this.cache.get(id);

            if (session != null) {
                sessions.add(session);
            }
        }

        return sessions;
    }

    public void purge() {
        this.cache.clear();
    }

    public SessionStorageStatistics getSessionStorageStatistics() {
        return DefaultSessionStorageStatisticsImpl.INVALID_STATISTICS;
    }
}
