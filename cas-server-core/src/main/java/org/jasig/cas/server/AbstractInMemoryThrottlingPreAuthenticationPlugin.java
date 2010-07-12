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

package org.jasig.cas.server;

import org.jasig.cas.server.login.LoginRequest;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of a PreAuthenticationPlugin and AuthenticationResponsePlugin that keeps track of a mapping
 * of IP Addresses to number of failures to authenticate.
 * <p>
 * Note, this class relies on an external method for decrementing the counts (i.e. a Quartz Job) and runs independent of the
 * threshold of the parent.

 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.5
 */
public abstract class AbstractInMemoryThrottlingPreAuthenticationPlugin extends AbstractThrottlingPreAuthenticationPlugin {

    private final ConcurrentMap<String, AtomicInteger> ipMap = new ConcurrentHashMap<String, AtomicInteger>();

    @Override
    protected final int findCount(final LoginRequest loginRequest) {
        final AtomicInteger existingValue = this.ipMap.get(constructKey(loginRequest));
        return existingValue == null ? 0 : existingValue.get();
    }

    @Override
    protected final void updateCount(final LoginRequest loginRequest) {
        final AtomicInteger newAtomicInteger = new AtomicInteger(1);
        final AtomicInteger oldAtomicInteger = this.ipMap.putIfAbsent(constructKey(loginRequest), newAtomicInteger);

        if (oldAtomicInteger != null) {
            oldAtomicInteger.incrementAndGet();
        }
    }

    protected abstract String constructKey(LoginRequest loginRequest);

    /**
     * This class relies on an external configuration to clean it up. It ignores the threshold data in the parent class.
     */
    public final void decrementCounts() {
        final Set<String> keys = this.ipMap.keySet();
        log.debug("Decrementing counts for throttler.  Starting key count: " + keys.size());

        for (final Iterator<String> iter = keys.iterator(); iter.hasNext();) {
            final String key = iter.next();
            final AtomicInteger integer = this.ipMap.get(key);
            final int  newValue = integer.decrementAndGet();

            if (log.isTraceEnabled()) {
                log.trace("Decrementing count for key [" + key + "]; starting count [" + integer + "]; ending count [" + newValue + "]");
            }

            if (newValue == 0) {
                iter.remove();
            }
        }
        log.debug("Done decrementing count for throttler.");
    }
}
