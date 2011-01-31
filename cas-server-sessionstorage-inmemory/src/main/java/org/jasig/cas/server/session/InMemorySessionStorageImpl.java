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

import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.util.Cleanable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
@Named("sessionStorage")
@Singleton
public final class InMemorySessionStorageImpl extends AbstractSessionStorage implements Cleanable {

    private Map<String, Session> sessions = new ConcurrentHashMap<String,Session>();

    private Map<String,String> accessIdToSessionIdMapping = new ConcurrentHashMap<String,String>();

    @Inject
    public InMemorySessionStorageImpl(final List<AccessFactory> accessFactories, final ServicesManager servicesManager) {
        super(accessFactories, servicesManager, new MultiUseOrTimeToLiveExpirationPolicy(21600));
    }

    public Session createSession(final AuthenticationResponse authenticationResponse) {
        final Session session = new InMemorySessionImpl(getExpirationPolicy(), getAccessFactories(), authenticationResponse.getAuthentications(), authenticationResponse.getPrincipal(), getServicesManager());
        this.sessions.put(session.getId(), session);
        return session;
    }

    public Session destroySession(final String sessionId) {
        Assert.notNull(sessionId);
        final Session session = this.sessions.remove(sessionId);

        if (session == null) {
            return null;
        }

        for (final Access access : session.getAccesses()) {
            this.accessIdToSessionIdMapping.remove(access.getId());
        }

        return session;
    }

    public Session findSessionBySessionId(final String sessionId) {
        Assert.notNull(sessionId);

        final Session session = this.sessions.get(sessionId);

        if (session == null || !session.isValid()) {
            return null;
        }

        return session;
    }

    // TODO note this will FAIL horribly for proxied sessions.  But we're not there yet.
    public Session updateSession(final Session session) {
        Assert.notNull(session);

        for (final Access access : session.getAccesses()) {
            this.accessIdToSessionIdMapping.put(access.getId(), session.getId());
        }

        return session;
    }

    public Session findSessionByAccessId(final String accessId) {
        Assert.notNull(accessId);
        final String sessionId = this.accessIdToSessionIdMapping.get(accessId);

        if (sessionId == null) {
            return null;
        }

        return findSessionBySessionId(sessionId);
    }

    public Set<Session> findSessionsByPrincipal(final String principalName) {
        Assert.notNull(principalName);

        final Set<Session> userSessions = new HashSet<Session>();

        for (final Session session : this.sessions.values()) {
            if (session.getPrincipal().getName().equals(principalName) && session.isValid()) {
                userSessions.add(session);
            }
        }
        
        return userSessions;
    }

    public void purge() {
        this.sessions.clear();
        this.accessIdToSessionIdMapping.clear();
    }

    public SessionStorageStatistics getSessionStorageStatistics() {
        final DefaultSessionStorageStatisticsImpl statistics = new DefaultSessionStorageStatisticsImpl(true);

        for (final Session session : this.sessions.values()) {
            if (session.isValid()) {
                statistics.incrementCountOfActiveSessions();
            } else {
                statistics.incrementCountOfInactiveSessions();
            }

            for (final Access access : session.getAccesses()) {
                if (access.isUsed()) {
                    statistics.incrementCountOfUsedAccesses();
                } else {
                    statistics.incrementCountOfUnusedAccesses();
                }
            }
        }

        return statistics;
    }

    @Scheduled(fixedDelay = 5000000)
    public void prune() {
        final Collection<Session> existingSessions = this.sessions.values();
        final List<Access> accessesToKill = new ArrayList<Access>();

        for (final Session session : existingSessions) {
            if (!session.isValid()) {
                this.sessions.remove(session.getId());
                accessesToKill.addAll(session.getAccesses());

                for (final Access access : session.getAccesses()) {
                    this.accessIdToSessionIdMapping.remove(access.getId());
                }
            }
        }

        for (final Access access : accessesToKill) {
            try {
                access.invalidate();
            } catch (final Exception e) {
                // make sure that nothing stops us from continuing
            }
        }
    }
}
