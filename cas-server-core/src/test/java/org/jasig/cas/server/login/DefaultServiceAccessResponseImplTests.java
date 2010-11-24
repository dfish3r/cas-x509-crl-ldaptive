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

import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.authentication.DefaultAuthenticationResponseImpl;
import org.jasig.cas.server.authentication.Message;
import org.jasig.cas.server.session.Access;
import org.jasig.cas.server.session.Session;
import org.junit.Test;

import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultServiceAccessResponseImplTests {

    private static final String CONST_SESSION_ID = "foo";

    @Test
    public void firstConstructor() {
        final Access access = mock(Access.class);
        final Session session = mock(Session.class);
        when(session.getId()).thenReturn(CONST_SESSION_ID);
        final DefaultServiceAccessResponseImpl response = new DefaultServiceAccessResponseImpl(access, Collections.<Access>emptyList(), session, new DefaultAuthenticationResponseImpl(Collections.<Credential, List<GeneralSecurityException>>emptyMap(), Collections.<Message>emptyList()));

        assertNotNull(response.getAccess());
        assertNotNull(response.getLoggedOutAccesses());
        assertNotNull(response.getAttributes());
        assertNotNull(response.getDate());
        assertNotNull(response.getSession());
        assertEquals(CONST_SESSION_ID, response.getSession().getId());
        assertNotNull(response.getGeneralSecurityExceptions());
        assertNotNull(response.getAuthenticationWarnings());
    }

    @Test
    public void secondConstructor() {
        final Access access = mock(Access.class);
        final DefaultServiceAccessResponseImpl response = new DefaultServiceAccessResponseImpl(access, Collections.<Access>emptyList(), new DefaultAuthenticationResponseImpl(Collections.<org.jasig.cas.server.authentication.Credential, java.util.List<java.security.GeneralSecurityException>>emptyMap(), Collections.<Message>emptyList()));

        assertNotNull(response.getAccess());
        assertNotNull(response.getLoggedOutAccesses());
        assertNotNull(response.getAttributes());
        assertNotNull(response.getDate());
        assertNull(response.getSession());
        assertNotNull(response.getGeneralSecurityExceptions());
        assertNotNull(response.getAuthenticationWarnings());
    }

    
}
