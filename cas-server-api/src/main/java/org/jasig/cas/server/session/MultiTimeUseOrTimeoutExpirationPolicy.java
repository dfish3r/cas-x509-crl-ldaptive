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
 * ExpirationPolicy that is based on certain number of uses of a ticket or a
 * certain time period for a ticket to exist.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class MultiTimeUseOrTimeoutExpirationPolicy implements ExpirationPolicy {

    /** The time to kill in millseconds. */
    @Min(1)
    private final long timeToKillInMilliSeconds;

    /** The maximum number of uses before expiration. */
    @Min(1)
    private final int numberOfUses;

    public MultiTimeUseOrTimeoutExpirationPolicy(final int numberOfUses, final long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
        this.numberOfUses = numberOfUses;
    }

    public boolean isExpired(final State state) {
        return (state.getUsageCount() >= this.numberOfUses)
            || (System.currentTimeMillis() - state.getLastUsedTime() >= this.timeToKillInMilliSeconds);
    }
}
