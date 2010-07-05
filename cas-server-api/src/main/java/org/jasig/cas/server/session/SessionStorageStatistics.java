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
 * Holds the statistics for this storage mechanism.
 * <p>
 * Note, these are snapshot statistics.  They are what is in the session storage, not lifetime stats.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface SessionStorageStatistics {

    /**
     * True if these are actual meaningful statistics.  False otherwise.
     * @return see above.
     */
    boolean isValidStatistics();

    /**
     * Returns the number of active sessions.  Depending on when it was last purged, this may be an estimate.
     *
     *
     * @return the estimated number of active sessions or -1 if unable to determine at all.
     */
    int getCountOfActiveSessions();

    /**
     * The number of sessions that have expired but have not yet been purged.
     *
     * @return the count, or -1 if unable to determine.
     */
    int getCountOfInactiveSessions();

    /**
     * The number of accesses that have not been used yet.
     *
     * @return the count, or -1 if unable to determine.
     */
    int getCountOfUnusedAccesses();

    /**
     * The number of accesses that have been used.
     *
     * @return the count, or -1 if unable to determine.
     */
    int getCountOfUsedAccesses();
}
