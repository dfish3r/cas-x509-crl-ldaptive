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

import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.util.TimeUnit;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Memcached or repcached backed implementation of the {@link org.jasig.cas.server.session.SessionStorage} interface.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class MemcachedSessionStorageImpl extends AbstractSerializableSessionStorageImpl {

    private static final String ACCESS_PREFIX = "ACCESSID::";

    private static final String ROOT_SESSION_PREFIX = "ROOTSESSIONID::";

    private static final String PRINCIPAL_SESSION_PREFIX = "PRINCIPALIDS::";

    @NotNull
    private final MemcachedClient memcachedClient;

    private boolean synchronizeUpdates = true;

    @Min(0)
    private final int sessionTimeOut;

    public MemcachedSessionStorageImpl(final List<InetSocketAddress> servers, final List<AccessFactory> accessFactories, final ServicesManager servicesManager, final int sessionTimeOut) throws IOException {
        this(servers, accessFactories, servicesManager, sessionTimeOut, TimeUnit.SECONDS);
    }

    public MemcachedSessionStorageImpl(final List<InetSocketAddress> servers, final List<AccessFactory> accessFactories, final ServicesManager servicesManager, final int sessionTimeOut, final TimeUnit timeUnit) throws IOException {
        super(accessFactories, servicesManager);
        this.memcachedClient = new MemcachedClient(new DefaultConnectionFactory(), servers);

        switch (timeUnit) {
            case SECONDS:
                this.sessionTimeOut = sessionTimeOut;
                break;

            case MILLISECONDS:
                this.sessionTimeOut = sessionTimeOut / 1000;
                break;

            case MINUTES:
                this.sessionTimeOut = sessionTimeOut * 60;
                break;

            default: // hours
                this.sessionTimeOut = sessionTimeOut * 60 * 60;
                break;
        }
    }


	private void handleSynchronousRequest(final Future f) {
	    try {
	        if (this.synchronizeUpdates) {
	            f.get();
	        }
	    } catch (final Exception e) {
	        // ignore these.
	    }
	}

    public Session createSession(final AuthenticationResponse authenticationResponse) {
        AbstractStaticSession.setExpirationPolicy(getExpirationPolicy());
        final Session session = new SerializableSessionImpl(authenticationResponse);

        this.memcachedClient.add(ROOT_SESSION_PREFIX + session.getId(), this.sessionTimeOut, session.getId());
        final List<String> ids = (List<String>) this.memcachedClient.get(PRINCIPAL_SESSION_PREFIX + session.getPrincipal().getName());

        if (ids != null) {
            ids.add(session.getId());
            handleSynchronousRequest(this.memcachedClient.set(PRINCIPAL_SESSION_PREFIX + session.getPrincipal().getName(), this.sessionTimeOut, ids));
        } else {
            final List<String> lids = new ArrayList<String>();
            lids.add(session.getId());
            handleSynchronousRequest(this.memcachedClient.set(PRINCIPAL_SESSION_PREFIX + session.getPrincipal().getName(), this.sessionTimeOut, lids));
        }

        handleSynchronousRequest(this.memcachedClient.set(session.getId(), this.sessionTimeOut, session));

        return session;
    }

    public SessionStorageStatistics getSessionStorageStatistics() {
        return new DefaultSessionStorageStatisticsImpl(false);
    }

    @Override
    protected Set<Session> findSessionsByPrincipalInternal(final String principalName) {
        final List<String> ids = (List<String>) this.memcachedClient.get(PRINCIPAL_SESSION_PREFIX + principalName);

        if (ids == null) {
            return Collections.emptySet();
        }

        final Collection<String> multiKeys = new ArrayList<String>();
        for (final String id : ids) {
            multiKeys.add(ROOT_SESSION_PREFIX + id);
        }

        final Map<String, Object> mapSessions = this.memcachedClient.getBulk(multiKeys);

        if (mapSessions.values().isEmpty()) {
            return Collections.emptySet();
        }

        final Collection<String> actualSessionIds = new ArrayList<String>();

        for (final Object o : mapSessions.values()) {
            actualSessionIds.add(o.toString());
        }

        final Map<String, Object> actualSessions = this.memcachedClient.getBulk(actualSessionIds);

        final Set<Session> sessions = new HashSet<Session>();

        for (final Object o : actualSessions.values()) {
            sessions.add((Session) o);
        }

        return sessions;
    }

    @Override
    protected Session findSessionByAccessIdInternal(final String accessId) {
        Assert.notNull(accessId);
        final String sessionId = (String) this.memcachedClient.get(ACCESS_PREFIX + accessId);

        if (sessionId == null) {
            return null;
        }

        final String rootSessionId = (String) this.memcachedClient.get(ROOT_SESSION_PREFIX + sessionId);

        if (rootSessionId == null) {
            return null;
        }

        final Session rootSession = (Session) this.memcachedClient.get(rootSessionId);

        if (rootSession == null) {
            return null;
        }

        if (rootSession.getId().equals(sessionId)) {
            if (rootSession.isValid()) {
                return rootSession;
            }

            return null;
        }

        final Session session= rootSession.findChildSessionById(sessionId);

        if (session == null || !session.isValid()) {
            return null;
        }

        return session;
    }

    @Override
    protected Session findSessionBySessionIdInternal(final String sessionId) {
        final String rootSessionId = (String) this.memcachedClient.get(ROOT_SESSION_PREFIX + sessionId);

        if (rootSessionId == null) {
            return null;
        }

        final Session rootSession = (Session) this.memcachedClient.get(rootSessionId);

        if (rootSession == null) {
            return null;
        }

        if (rootSession.getId().equals(sessionId)) {
            if (rootSession.isValid()) {
                return rootSession;
            }

            return null;
        }

        final Session session = rootSession.findChildSessionById(sessionId);

        if (session != null && session.isValid()) {
            return session;
        }

        return null;
    }

    @Override
    protected Session destroySessionInternal(final String sessionId) {
        // retrieve the root session id
        final String rootSessionId = (String) this.memcachedClient.get(ROOT_SESSION_PREFIX + sessionId);

        if (rootSessionId == null) {
            return null;
        }

        this.memcachedClient.delete(ROOT_SESSION_PREFIX + sessionId);

        //retrieve the root session
        final Session rootSession = (Session) this.memcachedClient.get(rootSessionId);

        // we couldn't find the root session!
        if (rootSession == null) {
            return null;
        }

        // if the root session *is* the current session
        if (rootSession.getId().equals(sessionId)) {
            this.memcachedClient.delete(sessionId);
            for (final Access access : rootSession.getAccesses()) {
                this.memcachedClient.delete(ACCESS_PREFIX + access.getId());
            }
            return rootSession;
        }

        final Session session = rootSession.findChildSessionById(sessionId);

        // we couldn't find the child session for some reason
        if (session == null) {
            return null;
        }

        for (final Access access : session.getAccesses()) {
            this.memcachedClient.delete(ACCESS_PREFIX + access.getId());
        }

        return session;
    }

    // TODO double check that this is correct.
    public Session updateSession(final Session session) {
        for (final Access access : session.getAccesses()) {
            this.memcachedClient.set(ACCESS_PREFIX + access.getId(), this.sessionTimeOut, session.getId());
        }

        final Session rootSession = session.getRootSession();
        handleSynchronousRequest(this.memcachedClient.set(rootSession.getId(), this.sessionTimeOut, rootSession));

        return session;
    }


    public void purge() {
        handleSynchronousRequest(this.memcachedClient.flush());
    }

    @PreDestroy
    public void destroy() {
        this.memcachedClient.shutdown();
    }

    /**
     * Determines whether we should "guarantee" that memcached finished before we return.  The default is to not wait
     * for a response from Memcached.  However, in absurdly fast environments this might mean that when you go to look
     * up the session, it might not exist yet.
     *
     * @param synchronizeUpdates true if you want to synchronize, false otherwise.
     */
    public void setSynchronizeUpdates(final boolean synchronizeUpdates) {
        this.synchronizeUpdates = synchronizeUpdates;
    }
}
