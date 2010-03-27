package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.login.LoginRequest;
import org.jasig.cas.server.util.Cleanable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class InMemorySessionStorageImpl extends AbstractSessionStorage implements Cleanable {

    private Map<String, Session> sessions = new ConcurrentHashMap<String,Session>();

    private Map<String,String> accessIdToSessionIdMapping = new ConcurrentHashMap<String,String>();

    public InMemorySessionStorageImpl(final List<AccessFactory> accessFactories) {
        super(accessFactories);
    }

    public Session createSession(final LoginRequest loginRequest, final Authentication authentication) throws InvalidatedSessionException {
        final Session session = new InMemorySessionImpl(getExpirationPolicy(), getAccessFactories(), authentication);
        this.sessions.put(session.getId(), session);
        return session;
    }

    public Session destroySession(final String sessionId) {
        Assert.notNull(sessionId);
        final Session session = this.sessions.get(sessionId);

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

    public Session updateSession(final Session session) {
        Assert.notNull(session);
        // TODO this might change when we handle changing the session id value.
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
            if (session.getAuthentication().getPrincipal().getName().equals(principalName) && session.isValid()) {
                userSessions.add(session);
            }
        }
        
        return userSessions;
    }

    public void purge() {
        this.sessions.clear();
        this.accessIdToSessionIdMapping.clear();
    }

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
