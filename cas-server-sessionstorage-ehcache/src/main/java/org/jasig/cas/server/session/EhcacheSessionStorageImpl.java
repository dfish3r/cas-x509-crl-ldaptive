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

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.session.*;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Implementation of the {@link SessionStorage} interface that supports using EHCache.
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class EhcacheSessionStorageImpl extends AbstractSerializableSessionStorageImpl {

    @NotNull
    private final Ehcache cache;

    @NotNull
    private final Ehcache cacheMappings;

    @NotNull
    private final Ehcache principalMappings;

    public EhcacheSessionStorageImpl(final Ehcache cache, final Ehcache cacheMappings, final Ehcache principalMappings, final List<AccessFactory> accessFactories, final ServicesManager servicesManager) {
        super(accessFactories, servicesManager);
        this.cache = cache;
        this.principalMappings = principalMappings;
        this.cacheMappings = cacheMappings;
    }

    public Session createSession(final AuthenticationResponse authenticationResponse) {
        final SerializableSessionImpl session = new SerializableSessionImpl(authenticationResponse);
        this.cache.put(new Element(session.getId(), session));
        this.cacheMappings.put(new Element(session.getId(), session.getId()));

        final List<String> sessions = new ArrayList<String>();

        final List<String> existingSessions = getValueFromElement(this.principalMappings.get(session.getPrincipal().getName()));

        if (existingSessions != null) {
            sessions.addAll(existingSessions);
        }

        sessions.add(session.getId());
        this.principalMappings.put(new Element(session.getPrincipal().getName(), sessions));

        return session;
    }

    public Session updateSession(final Session session) {
        // update access ids
        for (final Access access : session.getAccesses()) {
            this.cacheMappings.put(new Element(access.getId(), session.getId()));
        }

        // persist the parent session
        final Session rootSession = session.getRootSession();
        this.cache.put(new Element(rootSession.getId(), rootSession));

        return session;
    }

    public void purge() {
        this.cache.removeAll();
        this.cacheMappings.removeAll();
        this.principalMappings.removeAll();
    }

    public SessionStorageStatistics getSessionStorageStatistics() {
        return DefaultSessionStorageStatisticsImpl.INVALID_STATISTICS;
    }

    @Override
    protected Set<Session> findSessionsByPrincipalInternal(final String principalName) {
        Assert.notNull(principalName, "principalName cannot be null.");
        final List<String> sessionStrings = getValueFromElement(this.principalMappings.get(principalName));

        if (sessionStrings == null || sessionStrings.isEmpty()) {
            return Collections.emptySet();
        }

        final Set<Session> sessions = new HashSet<Session>();

        for (final String id : sessionStrings) {
            final Session session = getValueFromElement(this.cache.get(id));

            if (session != null) {
                sessions.add(session);
            }
        }

        return sessions;
    }

    @Override
    protected Session findSessionByAccessIdInternal(final String accessId) {
        Assert.notNull(accessId, "accessId cannot be null.");
        final String lowestLevelSession = getValueFromElement(this.cacheMappings.get(accessId));

        if (lowestLevelSession == null) {
            return null;
        }

        final String actualSession = getValueFromElement(this.cacheMappings.get(lowestLevelSession));

        if (actualSession == null) {
            return null;
        }

        final Session session = getValueFromElement(this.cache.get(actualSession));

        if (session == null) {
            return null;
        }

        if (actualSession.equals(session.getId())) {
            return session;
        }

        return session.findChildSessionById(lowestLevelSession);
    }

    @Override
    protected Session findSessionBySessionIdInternal(final String sessionId) {
        Assert.notNull(sessionId, "sessionId cannot be null.");
        final String actualSession = getValueFromElement(this.cacheMappings.get(sessionId));

        if (actualSession == null) {
            return null;
        }

        return getValueFromElement(this.cache.get(actualSession));
    }

    @Override
    protected Session destroySessionInternal(final String sessionId) {
        Assert.notNull(sessionId, "sessionId cannot be null.");
        // find the session
        final Session session = findSessionBySessionIdInternal(sessionId);

        if (session == null) {
            return null;
        }

        // remove its mappings
        this.cacheMappings.remove(sessionId);

        if (session.isRoot()) {
            final Element e = this.principalMappings.get(session.getPrincipal().getName());
            final List<String> sessions = getValueFromElement(e);

            // remove any listing in the principal map
            if (sessions != null) {
                sessions.remove(sessionId);
                this.principalMappings.put(new Element(session.getPrincipal().getName(), sessions));
            }

            // remove it
            this.cache.remove(sessionId);
        }

        return session;
    }

    protected <T> T getValueFromElement(final Element e) {
        if (e == null) {
            return null;
        }

        return (T) e.getValue();
    }
}
