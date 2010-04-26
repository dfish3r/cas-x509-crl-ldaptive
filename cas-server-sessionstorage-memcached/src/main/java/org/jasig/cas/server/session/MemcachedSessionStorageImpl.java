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
import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.login.LoginRequest;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Memcached or repcached backed implementation of the {@link org.jasig.cas.server.session.SessionStorage} interface.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class MemcachedSessionStorageImpl extends AbstractSessionStorage {

    private static final String ACCESS_PREFIX = "ACCESSID::";

    private static final String ROOT_SESSION_PREFIX = "ROOTSESSIONID::";

    @NotNull
    private final MemcachedClient memcachedClient;

    private boolean synchronizeUpdates = true;

    @Min(0)
    private final int sessionTimeOut;

    public MemcachedSessionStorageImpl(final List<InetSocketAddress> servers, final List<AccessFactory> accessFactories,  final int sessionTimeOut) throws IOException {
        this(servers, accessFactories, sessionTimeOut, TimeUnit.SECONDS);
    }

    public MemcachedSessionStorageImpl(final List<InetSocketAddress> servers, final List<AccessFactory> accessFactories, final int sessionTimeOut, final TimeUnit timeUnit) throws IOException {
        super(accessFactories);
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

            case HOURS:
                this.sessionTimeOut = sessionTimeOut * 60 * 60;
                break;

            default:
                throw new IllegalArgumentException("We currently only support SECONDS, MINUTES, HOURS, or MILLISECONDS as the TimeUnit");
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

    public Session createSession(final Authentication authentication) {
        // TODO we need a better way to set this.
        AbstractStaticSession.setAccessFactories(getAccessFactories());
        AbstractStaticSession.setExpirationPolicy(getExpirationPolicy());
        final Session session = new MemcachedSessionImpl(authentication);

        this.memcachedClient.add(ROOT_SESSION_PREFIX + session.getId(), this.sessionTimeOut, session.getId());
        handleSynchronousRequest(this.memcachedClient.set(session.getId(), this.sessionTimeOut, session));

        return session;
    }

    public Session destroySession(final String sessionId) {
        // retrieve the root session id
        final String rootSessionId = (String) this.memcachedClient.get(ROOT_SESSION_PREFIX + sessionId);

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

    public Session findSessionBySessionId(final String sessionId) {
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

    public Session updateSession(final Session session) {
        for (final Access access : session.getAccesses()) {
            this.memcachedClient.set(ACCESS_PREFIX + access.getId(), this.sessionTimeOut, session.getId());
        }

        final Session rootSession = session.getRootSession();
        handleSynchronousRequest(this.memcachedClient.set(rootSession.getId(), this.sessionTimeOut, rootSession));

        return session;
    }

    public Session findSessionByAccessId(final String accessId) {
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

    public Set<Session> findSessionsByPrincipal(final String principalName) {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void purge() {
        handleSynchronousRequest(this.memcachedClient.flush());
    }

    @PreDestroy
    public void destroy() {
        this.memcachedClient.shutdown(2, TimeUnit.SECONDS);
    }

    /**
     * Determines whether we should "guarantee" that memcache finished before we return.  The default is to not wait
     * for a response from Memcached.  However, in absurdly fast environments this might mean that when you go to look
     * up the session, it might not exist yet.
     *
     * @param synchronizeUpdates true if you want to synchronize, false otherwise.
     */
    public void setSynchronizeUpdates(final boolean synchronizeUpdates) {
        this.synchronizeUpdates = synchronizeUpdates;
    }
}
