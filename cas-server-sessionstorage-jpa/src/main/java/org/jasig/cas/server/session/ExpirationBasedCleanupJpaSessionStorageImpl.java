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

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses a complex, more memory-intensive algorithm for cleaning out old session objects based on the actual expiration object.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
@Named("sessionStorage")
public final class ExpirationBasedCleanupJpaSessionStorageImpl extends AbstractJpaSessionStorageImpl {

    @Min(1)
    private int batchSize = 100;

    @Inject
    public ExpirationBasedCleanupJpaSessionStorageImpl(final List<AccessFactory> accessFactories, final ServicesManager servicesManager) {
        super(accessFactories, servicesManager);
    }

    @Transactional
    @Scheduled(fixedDelay = 5000000)
    public void prune() {
        final Query query  = getEntityManager().createQuery("select s from session s where s.parentSession is null order by s.id");
        query.setMaxResults(this.batchSize);

        boolean moreResults = true;
        for (int page = 0; moreResults; page++) {
            query.setFirstResult(page * this.batchSize);
            final List<Session> listSessions = (List<Session>) query.getResultList();
            final List<String> ids = new ArrayList<String>();

            for (final Session session : listSessions) {
                ids.add(session.getId());
            }

            getEntityManager().createQuery("delete from session s where s.sessionId in (:idList)").setParameter("idList", ids).executeUpdate();

            if (listSessions.size() < this.batchSize) {
                moreResults = false;
            }
        }
    }

    @Override
    protected void calculateStatisticsInformation(final DefaultSessionStorageStatisticsImpl sessionStorageStatistics) {
        // TODO we need to calculate this
    }

    public void setBatchSize(final int batchSize) {
        this.batchSize = batchSize;
    }
}
