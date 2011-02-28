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
import org.opensaml.DefaultBootstrap;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class Saml1TokenServiceAccessRequestImplTests {

    @Test
    public void properXmlWithToken() throws Exception {
        DefaultBootstrap.bootstrap();
        final String xml = "<samlp:Request xmlns:samlp=\"urn:oasis:names:tc:SAML:1.0:protocol\" MajorVersion=\"1\" MinorVersion=\"1\" RequestID=\"_192.168.16.51.1024506224022\" IssueInstant=\"2002-06-19T17:03:44.022Z\">\n" +
                "       <samlp:AssertionArtifact>\n" +
                "         artifact\n" +
                "       </samlp:AssertionArtifact>\n" +
                "     </samlp:Request>";


        final Saml11TokenServiceAccessRequestImpl request = new Saml11TokenServiceAccessRequestImpl(xml, "127.0.0.1");
        assertEquals("artifact", request.getToken());
        assertEquals("_192.168.16.51.1024506224022", request.getRequestId());
        assertFalse(request.isPassiveAuthentication());
        assertFalse(request.isProxiedRequest());
        assertTrue(request.isValid());
        assertFalse(request.isLongTermLoginRequest());
        assertNull(request.getServiceId());
        assertNull(request.getPassiveAuthenticationRedirectUrl());
    }
}
