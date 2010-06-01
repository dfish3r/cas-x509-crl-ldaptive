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

import static junit.framework.Assert.*;
import org.junit.Test;

/**
 * Base class which can hold some basic tests as well as helper methods.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractExpirationPolicyTests {

    private final ExpirationPolicy expirationPolicy;

    protected AbstractExpirationPolicyTests(final ExpirationPolicy expirationPolicy) {
        this.expirationPolicy = expirationPolicy;
    }
    
    protected final ExpirationPolicy getExpirationPolicy() {
        return this.expirationPolicy;
    }

    protected final void sleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch(final InterruptedException e) {
            //nothing to do
        }
    }

    @Test
    public void immediateUsage() {
        final State state = new SimpleStateImpl();
        assertFalse(this.expirationPolicy.isExpired(state));
    }
}
