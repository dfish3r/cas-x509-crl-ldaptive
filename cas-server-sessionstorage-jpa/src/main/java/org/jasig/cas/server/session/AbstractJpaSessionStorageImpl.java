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
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base class for JPA-backed implementation of the {@link org.jasig.cas.server.session.SessionStorage}.

 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractJpaSessionStorageImpl extends AbstractSessionStorage implements Cleanable {

    @PersistenceContext
    @NotNull
    private EntityManager entityManager;

    public AbstractJpaSessionStorageImpl(final List<AccessFactory> accessFactories, final ServicesManager servicesManager) {
        super(accessFactories, servicesManager, new MultiUseOrTimeToLiveExpirationPolicy(21600));
        JpaSessionImpl.setAccessFactories(getAccessFactories());
        JpaSessionImpl.setServicesManager(servicesManager);
    }

    protected final EntityManager getEntityManager() {
        return this.entityManager;
    }

    public final Session destroySession(final String sessionId) {
        try {
            final Session session = (Session) this.entityManager.createQuery("select s from session s where s.sessionId = :sessionId").setParameter("sessionId", sessionId).getSingleResult();
            this.entityManager.remove(session);
            return session;
        } catch (final Exception e) {
            return null;
        }
    }

    public final Session findSessionBySessionId(final String sessionId) {
        try {
            return (Session) this.entityManager.createQuery("select s from session s where s.sessionId = :sessionId").setParameter("sessionId", sessionId).getSingleResult();
        } catch (final Exception e) {
            return null;
        }
    }

    public final Session updateSession(final Session session) {
        return this.entityManager.merge(session);
    }

    public final Session findSessionByAccessId(final String accessId) {
        try {
            return (Session) this.entityManager.createQuery("select s from session s, IN(s.casProtocolAccesses) c where c.id = :accessId").setParameter("accessId", accessId).getSingleResult();
        } catch (final Exception e) {
            return null;
        }
    }

    public final Session createSession(final AuthenticationResponse authenticationResponse) {
        JpaSessionImpl.setExpirationPolicy(getExpirationPolicy());
        final Session session = new JpaSessionImpl(authenticationResponse);
        return this.entityManager.merge(session);
    }

    // TODO replace this with the query stuff
    public final Set<Session> findSessionsByPrincipal(final String principalName) {
        Assert.notNull(principalName);
        final List<Session> listSessions = (List<Session>) this.entityManager.createQuery("select s from session s where s.attributePrincipal.name = :name").setParameter("name", principalName).getResultList();
        return new HashSet<Session>(listSessions);
    }

    public final SessionStorageStatistics getSessionStorageStatistics() {
        final Long countOfUnused = (Long) getEntityManager().createQuery("select count(a) from casProtocolAccess a where a.validationStatus = 'NOT_VALIDATED'").getSingleResult();
        final Long countOfUsed = (Long) getEntityManager().createQuery("select count(a) from casProtocolAccess a where a.validationStatus <> 'NOT_VALIDATED'").getSingleResult();


        final DefaultSessionStorageStatisticsImpl statistics = new DefaultSessionStorageStatisticsImpl(true);
        statistics.setCountOfUnusedAccesses(countOfUnused.intValue());
        statistics.setCountOfUsedAccesses(countOfUsed.intValue());

        calculateStatisticsInformation(statistics);

        return statistics;
    }

    public final void purge() {
        this.entityManager.createQuery("delete from session s").executeUpdate();
    }

    /**
     * Allows subclasses to calculate the statistic information for sessions.  The access ones are already calculated via the method above.
     *
     * @param sessionStorageStatistics the storage statistics to update.
     */
    protected abstract void calculateStatisticsInformation(DefaultSessionStorageStatisticsImpl sessionStorageStatistics);
}
