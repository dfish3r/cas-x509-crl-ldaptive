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

import org.junit.Test;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class LongTermSessionDelegatingExpirationPolicyTests extends AbstractExpirationPolicyTests {

    public LongTermSessionDelegatingExpirationPolicyTests() {
        super(new LongTermSessionDelegatingExpirationPolicy(new NeverExpiresExpirationPolicy(), new HardTimeoutExpirationPolicy(5)));
    }

    @Test
    public void testLongTermExpiration() {
        final SimpleStateImpl s = new SimpleStateImpl();
        s.setLongTermSessionExists(true);
        s.updateState();
        sleep(8);
        assertFalse(getExpirationPolicy().isExpired(s));
    }

    @Test
    public void testShortTermExpiration() {
        final SimpleStateImpl s = new SimpleStateImpl();
        s.setLongTermSessionExists(false);
        s.updateState();
        sleep(8);
        assertTrue(getExpirationPolicy().isExpired(s));
    }
}
