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

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * This should support tests for all the {@link org.jasig.cas.server.session.State} implementations.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractStateTests {

    private State state;

    @Before
    public final void initialize() {
        this.state = getNewState();
    }

    /**
     * Returns a new instance of a state object.
     * @return
     */
    protected abstract State getNewState();

    @Test
    public final void initialState() {
        final long currentTime = System.currentTimeMillis();
        assertTrue(this.state.getCreationTime() - currentTime <= 5);
        assertEquals(0, this.state.getUsageCount());
        assertTrue(this.state.getLastUsedTime() - currentTime <= 5);
        assertFalse(this.state.longTermAuthenticationExists());
    }

    @Test
    public final void updatedState() {
        final long currentTime = System.currentTimeMillis();
        this.state.updateState();
        assertEquals(currentTime, this.state.getCreationTime());
        assertEquals(1, this.state.getUsageCount());
        assertFalse(this.state.longTermAuthenticationExists());
        assertTrue(this.state.getLastUsedTime() >= this.state.getCreationTime());
    }
}
