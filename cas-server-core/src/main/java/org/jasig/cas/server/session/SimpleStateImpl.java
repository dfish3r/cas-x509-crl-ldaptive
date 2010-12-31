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

import java.io.Serializable;

/**
 * Default implementation of the {@link org.jasig.cas.server.session.State} interface suitable for in-memory storage or
 * serialization.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class SimpleStateImpl implements State, Serializable {

    private volatile int usageCount = 0;

    private final long creationTime = System.currentTimeMillis();

    private volatile long lastUsedTime = this.creationTime;

    private boolean longTermSession;

    public synchronized void updateState() {
        this.usageCount++;
        this.lastUsedTime = System.currentTimeMillis();
    }

    public int getUsageCount() {
        return this.usageCount;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public long getLastUsedTime() {
        return this.lastUsedTime;
    }

    public void setLongTermSessionExists(final boolean longTermSession) {
        this.longTermSession = longTermSession;
    }

    public boolean longTermAuthenticationExists() {
        return this.longTermSession;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleStateImpl that = (SimpleStateImpl) o;

        if (creationTime != that.creationTime) return false;
        if (lastUsedTime != that.lastUsedTime) return false;
        if (longTermSession != that.longTermSession) return false;
        if (usageCount != that.usageCount) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = usageCount;
        result = 31 * result + (int) (creationTime ^ (creationTime >>> 32));
        result = 31 * result + (int) (lastUsedTime ^ (lastUsedTime >>> 32));
        result = 31 * result + (longTermSession ? 1 : 0);
        return result;
    }
}
