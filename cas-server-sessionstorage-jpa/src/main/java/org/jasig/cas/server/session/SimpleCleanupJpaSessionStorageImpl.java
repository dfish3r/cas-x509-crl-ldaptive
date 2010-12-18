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

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Note, that the purge method supports a more simplified method than you can possibly configured a session with as
 * the {@link org.jasig.cas.server.session.ExpirationPolicy}.  This is to ensure a fast clean up of the storage mechanism.
 * If one uses a complex ExpirationPolicy, one should set the timeout or purge count to be significantly higher so that it does not
 * trigger something to be deleted before its time.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class SimpleCleanupJpaSessionStorageImpl extends AbstractJpaSessionStorageImpl {

    @Min(1)
    private long purgeTimeOut = 21600000;

    @Min(1)
    private int purgeMaxCount = Integer.MAX_VALUE;

    @Autowired
    public SimpleCleanupJpaSessionStorageImpl(final List<AccessFactory> accessFactories, final ServicesManager servicesManager) {
        super(accessFactories, servicesManager);
    }

    public void prune() {
        getEntityManager().createQuery("delete From session s where (s.state.creationTime + :timeOut) >= :currentTime or s.state.count > :maxCount").setParameter("timeOut", this.purgeTimeOut).setParameter("currentTime", System.currentTimeMillis()).setParameter("maxCount", this.purgeMaxCount).executeUpdate();
    }

    public void setPurgeTimeOut(final long purgeTimeOut) {
        this.purgeTimeOut = purgeTimeOut;
    }

    public void setPurgeMaxCount(final int purgeMaxCount) {
        this.purgeMaxCount = purgeMaxCount;
    }
}
