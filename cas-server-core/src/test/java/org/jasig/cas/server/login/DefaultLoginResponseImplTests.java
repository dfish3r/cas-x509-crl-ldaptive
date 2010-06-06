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

import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.authentication.Message;
import org.junit.Test;

import java.security.GeneralSecurityException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultLoginResponseImplTests {

    private static final String CONST_SESSION_ID = "sessionId";

    @Test
    public void firstConstructor() {
        final AuthenticationResponse mockedAuthenticationResponse = mock(AuthenticationResponse.class);
        final Message message = mock(Message.class);
        when(mockedAuthenticationResponse.getAuthenticationMessages()).thenReturn(Arrays.asList(message));
        when(mockedAuthenticationResponse.getGeneralSecurityExceptions()).thenReturn(Arrays.asList(new GeneralSecurityException()));

        final DefaultLoginResponseImpl login = new DefaultLoginResponseImpl(CONST_SESSION_ID, mockedAuthenticationResponse);

        assertEquals(CONST_SESSION_ID, login.getSessionId());
        assertNotNull(login.getGeneralSecurityExceptions());
        assertNotNull(login.getAuthenticationWarnings());
        assertNotNull(login.getDate());
        assertNotNull(login.getAttributes());
        assertTrue(login.getGeneralSecurityExceptions().size() == 1);
        assertTrue(login.getAuthenticationWarnings().size() == 1);
    }

    @Test
    public void secondConstructor() {
        final AuthenticationResponse mockedAuthenticationResponse = mock(AuthenticationResponse.class);
        when(mockedAuthenticationResponse.getAuthenticationMessages()).thenReturn(Collections.<Message>emptyList());
        when(mockedAuthenticationResponse.getGeneralSecurityExceptions()).thenReturn(Arrays.asList(new GeneralSecurityException()));

        final DefaultLoginResponseImpl login = new DefaultLoginResponseImpl(mockedAuthenticationResponse);

        assertNull(login.getSessionId());
        assertNotNull(login.getGeneralSecurityExceptions());
        assertNotNull(login.getAuthenticationWarnings());
        assertNotNull(login.getDate());
        assertNotNull(login.getAttributes());
        assertTrue(login.getGeneralSecurityExceptions().size() == 1);
    }
}
