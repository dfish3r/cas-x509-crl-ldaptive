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
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class CasServiceAccessRequestImplTests {

    @Test
    public void gettersAndSetters() {
        final String CONST_SESSION_ID = "sessionId";
        final String CONST_SERVICE_ID = "constServiceId";
        final String CONST_REMOTE_IP = "127.0.0.1";
        final CasServiceAccessRequestImpl casServiceAccessRequest = new CasServiceAccessRequestImpl(CONST_SESSION_ID, CONST_SERVICE_ID, CONST_REMOTE_IP);
        assertEquals(CONST_SESSION_ID, casServiceAccessRequest.getSessionId());
        assertEquals(CONST_SERVICE_ID, casServiceAccessRequest.getServiceId());
        assertEquals(CONST_REMOTE_IP, casServiceAccessRequest.getRemoteIpAddress());
        assertFalse(casServiceAccessRequest.isPassiveAuthentication());
        assertFalse(casServiceAccessRequest.isPostRequest());
        assertTrue(casServiceAccessRequest.isProxiedRequest());
        assertFalse(casServiceAccessRequest.isForceAuthentication());
        assertFalse(casServiceAccessRequest.isLongTermLoginRequest());
        assertTrue(casServiceAccessRequest.isValid());
        assertTrue(casServiceAccessRequest.isAccessRequest());
    }

    @Test
    public void equalsAndHashcode() {
        final String CONST_SESSION_ID = "sessionId";
        final String CONST_SERVICE_ID = "constServiceId";
        final String CONST_REMOTE_IP = "127.0.0.1";
        final CasServiceAccessRequestImpl casServiceAccessRequest = new CasServiceAccessRequestImpl(CONST_SESSION_ID, CONST_SERVICE_ID, CONST_REMOTE_IP);
        final CasServiceAccessRequestImpl casServiceAccessRequest2 = new CasServiceAccessRequestImpl(CONST_SESSION_ID, CONST_SERVICE_ID, CONST_REMOTE_IP);
        assertEquals(casServiceAccessRequest, casServiceAccessRequest2);
        assertEquals(casServiceAccessRequest.hashCode(), casServiceAccessRequest2.hashCode());
    }
}
