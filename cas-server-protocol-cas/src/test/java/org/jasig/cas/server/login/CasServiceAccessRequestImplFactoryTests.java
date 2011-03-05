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
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class CasServiceAccessRequestImplFactoryTests {

    private CasServiceAccessRequestImplFactory factory = new CasServiceAccessRequestImplFactory();

    @Test
    public void getAccess() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final String CONST_SESSION_ID = "sessionId";
        final String CONST_REMOTE_IP = "127.0.0.1";
        request.setParameter("renew", "true");
//        request.setParameter("gateway", "false");
        request.setParameter("service", "http://www.cnn.com");

        final CasServiceAccessRequestImpl casServiceAccessRequest = (CasServiceAccessRequestImpl) factory.getServiceAccessRequest(CONST_SESSION_ID, CONST_REMOTE_IP, request.getParameterMap());
        assertTrue(casServiceAccessRequest.isForceAuthentication());
        assertFalse(casServiceAccessRequest.isPassiveAuthentication());
        assertEquals("http://www.cnn.com", casServiceAccessRequest.getServiceId());
        assertEquals(CONST_REMOTE_IP, casServiceAccessRequest.getRemoteIpAddress());
        assertEquals(CONST_SESSION_ID, casServiceAccessRequest.getSessionId());
    }
}
