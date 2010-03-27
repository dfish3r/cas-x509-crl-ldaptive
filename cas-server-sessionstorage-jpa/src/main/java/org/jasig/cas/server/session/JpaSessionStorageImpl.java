package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.util.Cleanable;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * JPA-backed implementation of the {@link org.jasig.cas.server.session.SessionStorage}.
 * <p>
 * Note, that the purge method supports a more simplified method than you can possibly configured a session with as
 * the {@link org.jasig.cas.server.session.ExpirationPolicy}.  This is to ensure a fast clean up of the storage mechanism.
 * If one uses a complex ExpirationPolicy, one should set the timeout or purge count to be significantly higher so that it does not
 * trigger something to be deleted before its time.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
//TODO future improvement is to actually use the expiration policy
public final class JpaSessionStorageImpl extends AbstractSessionStorage implements Cleanable {

    @PersistenceContext
    @NotNull
    private EntityManager entityManager;

    @Min(1)
    private long purgeTimeOut = 21600000;

    @Min(1)
    private int purgeMaxCount = Integer.MAX_VALUE;

    public JpaSessionStorageImpl(final List<AccessFactory> accessFactories) {
        super(accessFactories);
    }

    public Session destroySession(final String sessionId) {
        try {
            final Session session = (Session) this.entityManager.createQuery("select s from session s where s.sessionId = :sessionId").setParameter("sessionId", sessionId).getSingleResult();
            this.entityManager.remove(session);
            return session;
        } catch (final Exception e) {
            return null;
        }
    }

    public Session findSessionBySessionId(final String sessionId) {
        try {
            return (Session) this.entityManager.createQuery("select s from session s where s.sessionId = :sessionId").setParameter("sessionId", sessionId).getSingleResult();
        } catch (final Exception e) {
            return null;
        }
    }

    public Session updateSession(final Session session) {
        return this.entityManager.merge(session);
    }

    // TODO fix this once we have accesses working.
    public Session findSessionByAccessId(final String accessId) {
        return null;
        /*
        try {
            return (Session) this.entityManager.createQuery("select s from session s, IN(s.casProtocolAccesses) c where c.id = :accessId").setParameter("accessId", accessId).getSingleResult();
        } catch (final Exception e) {
            return null;
        } */
    }

    public Session createSession(final Authentication authentication) {
        JpaSessionImpl.setAccessFactories(getAccessFactories());
        JpaSessionImpl.setExpirationPolicy(getExpirationPolicy());

        final Session session = new JpaSessionImpl(authentication);
        return this.entityManager.merge(session);
    }

    public Set<Session> findSessionsByPrincipal(final String principalName) {
        Assert.notNull(principalName);
        final List<Session> listSessions = (List<Session>) this.entityManager.createQuery("select s from session s where s.authentication.attributePrincipal.name = :name").setParameter("name", principalName).getResultList();
        return new HashSet<Session>(listSessions);
    }

    public void purge() {
        this.entityManager.createQuery("delete from session s").executeUpdate();
    }

    public void prune() {
        this.entityManager.createQuery("Delete From session s where (s.state.creationTime + :timeOut) >= :currentTime or s.state.count > :maxCount").setParameter("timeOut", this.purgeTimeOut).setParameter("currentTime", System.currentTimeMillis()).setParameter("maxCount", this.purgeMaxCount).executeUpdate();
    }

    public void setPurgeTimeOut(final long purgeTimeOut) {
        this.purgeTimeOut = purgeTimeOut;
    }

    public void setPurgeMaxCount(final int purgeMaxCount) {
        this.purgeMaxCount = purgeMaxCount;
    }
}
