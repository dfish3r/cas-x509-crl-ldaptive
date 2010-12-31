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

/**
 * Default (and probably the only needed version) of the {@link org.jasig.cas.server.session.SessionStorageStatistics}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultSessionStorageStatisticsImpl implements SessionStorageStatistics {

    public static final SessionStorageStatistics INVALID_STATISTICS = new DefaultSessionStorageStatisticsImpl(false);

    private final boolean validStatistics;

    private int countOfActiveSessions;

    private int countOfInactiveSessions;

    private int countOfUnusedAccesses;

    private int countOfUsedAccesses;

    public DefaultSessionStorageStatisticsImpl(final boolean validStatistics) {
        this.validStatistics = validStatistics;

        if (!validStatistics) {
            this.countOfInactiveSessions = -1;
            this.countOfUnusedAccesses = -1;
            this.countOfUsedAccesses = -1;
            this.countOfActiveSessions = -1;
        }
    }

    public boolean isValidStatistics() {
        return this.validStatistics;
    }

    public int getCountOfActiveSessions() {
        return this.countOfActiveSessions;
    }

    public int getCountOfInactiveSessions() {
        return this.countOfInactiveSessions;
    }

    public int getCountOfUnusedAccesses() {
        return this.countOfUnusedAccesses;
    }

    public int getCountOfUsedAccesses() {
        return this.countOfUsedAccesses;
    }

    public void incrementCountOfActiveSessions() {
        this.countOfActiveSessions++;
    }

    public void incrementCountOfInactiveSessions() {
        this.countOfInactiveSessions++;
    }

    public void incrementCountOfUnusedAccesses() {
        this.countOfUnusedAccesses++;
    }

    public void incrementCountOfUsedAccesses() {
        this.countOfUsedAccesses++;
    }

    public void setCountOfActiveSessions(final int countOfActiveSessions) {
        this.countOfActiveSessions = countOfActiveSessions;
    }

    public void setCountOfInactiveSessions(final int countOfInactiveSessions) {
        this.countOfInactiveSessions = countOfInactiveSessions;
    }

    public void setCountOfUnusedAccesses(final int countOfUnusedAccesses) {
        this.countOfUnusedAccesses = countOfUnusedAccesses;
    }

    public void setCountOfUsedAccesses(final int countOfUsedAccesses) {
        this.countOfUsedAccesses = countOfUsedAccesses;
    }
}
