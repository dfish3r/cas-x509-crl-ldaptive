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

package org.jasig.cas.server.login;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultLoginRequestImplTests {

    private final DefaultLoginRequestImpl impl;

    private static final String CONST_SESSION_ID = "foo";

    private static final String CONST_REMOTE_IP_ADDRESS = "192.168.0.1";

    private static final boolean CONST_FORCE_AUTHENTICATION = true;

    public DefaultLoginRequestImplTests() {
            this.impl = new DefaultLoginRequestImpl(CONST_SESSION_ID, CONST_REMOTE_IP_ADDRESS, CONST_FORCE_AUTHENTICATION);
    }

    @Test
    public void getters() {
        assertEquals(CONST_SESSION_ID, this.impl.getSessionId());
        assertEquals(CONST_REMOTE_IP_ADDRESS, this.impl.getRemoteIpAddress());
        assertEquals(CONST_FORCE_AUTHENTICATION, this.impl.isForceAuthentication());
    }
}
