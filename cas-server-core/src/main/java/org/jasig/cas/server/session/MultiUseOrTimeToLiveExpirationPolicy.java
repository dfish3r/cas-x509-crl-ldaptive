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

import org.jasig.cas.server.authentication.Authentication;

import javax.validation.constraints.Min;

/**
 * Allows for an item to either be used <code>maxNumberOfTimes</code> or <code>timeToLive</code> in seconds.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class MultiUseOrTimeToLiveExpirationPolicy implements ExpirationPolicy {

    @Min(1)
    private int maxNumberOfUses;

    @Min(1)
    private long timeToLive;

    /**
     * Creates a new MultiUseOrTimeToLiveExpirationPolicy with the maximum number of uses or the time to live (in milliseconds).
     *
     * @param maxNumberOfUses the maximum number of uses
     * @param timeToLive time to live, in milliseconds.
     */
    public MultiUseOrTimeToLiveExpirationPolicy(final int maxNumberOfUses, final long timeToLive) {
        this.maxNumberOfUses = maxNumberOfUses;
        this.timeToLive = timeToLive;
    }

    public MultiUseOrTimeToLiveExpirationPolicy(final long timeToLive) {
        this(Integer.MAX_VALUE, timeToLive);

    }

    public boolean isExpired(final State state) {
        return (state.getUsageCount() >= this.maxNumberOfUses) || (System.currentTimeMillis() - state.getCreationTime() >= this.timeToLive);
    }
}
