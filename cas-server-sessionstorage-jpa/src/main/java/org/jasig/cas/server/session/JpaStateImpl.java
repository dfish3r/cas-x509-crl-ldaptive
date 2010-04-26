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

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JPA-annotated implementation of the {@link org.jasig.cas.server.session.State} interface.
 *
 * @author Scott Battaglia
 * @version $Revision$ Date$
 * @since 4.0.0
 */
@Embeddable
public final class JpaStateImpl implements State {

    @Column(name="session_state_usage_count", insertable = true, updatable = true, nullable = false)
    private volatile int count = 0;

    @Column(name="session_state_creation", insertable = true, updatable = false, nullable = false)
    private volatile long creationTime = System.currentTimeMillis();

    @Column(name="session_state_last_used",insertable = true, updatable = true, nullable = false)
    private long lastUsedTime = System.currentTimeMillis();

    public synchronized void updateState() {
        this.count++;
        this.lastUsedTime = System.currentTimeMillis();
    }

    public int getUsageCount() {
        return this.count;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public long getLastUsedTime() {
        return this.lastUsedTime;
    }
}
