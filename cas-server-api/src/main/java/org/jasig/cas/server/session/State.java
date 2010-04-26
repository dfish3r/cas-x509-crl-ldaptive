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
 * Holds the internal state of an object so that an {@link org.jasig.cas.server.session.ExpirationPolicy} can be
 * used in conjunction with the object to determine its current status.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 *
 */
public interface State {

    /**
     * Update the internal state.  Only the object that OWNS this state should call this method.
     */
    void updateState();

    /**
     * Return the number of times this thing was "used."  Used is subjective and varies by thing holding state.
     * @return the usage count, >= 0
     */
    int getUsageCount();

    /**
     * The time the state was initialized.
     *
     * @return the creation time, in milliseconds past the epoch.
     */
    long getCreationTime();

    /**
     * The time the state was last "used."
     *
     * @return the time last used.  Cannot be less than the creation time.
     */
    long getLastUsedTime();

    /**
     * Its true, if one of the authentication's is a long term authentication request.
     *
     * @return true, if a long term authentication exists, false otherwise.
     */
    boolean longTermAuthenticationExists();
}
