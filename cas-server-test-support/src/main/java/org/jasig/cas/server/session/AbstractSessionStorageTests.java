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

import junit.framework.TestCase;
import org.jasig.cas.TestUtils;
import org.jasig.cas.server.authentication.*;
import org.jasig.cas.server.util.Cleanable;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.security.GeneralSecurityException;
import java.util.*;

/**
 * Abstract class to ensure all session storage are run against the same tests.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSessionStorageTests extends TestCase {

    private final AttributePrincipalFactory attributePrincipalFactory = getAttributePrincipalFactory();

    private final AuthenticationFactory authenticationFactory = getAuthenticationFactory();

    private SessionStorage sessionStorage;

    protected abstract SessionStorage getSessionStorage();

    protected abstract AuthenticationFactory getAuthenticationFactory();

    protected abstract AttributePrincipalFactory getAttributePrincipalFactory();

    protected final AuthenticationResponse getAuthenticationResponse(final String principal) {
        final AuthenticationResponse response = mock(AuthenticationResponse.class);
        when(response.getAuthentications()).thenReturn(new HashSet<Authentication>(Arrays.asList(getConstructedAuthentication())));
        when(response.getPrincipal()).thenReturn(TestUtils.getPrincipal(principal));
        when(response.getGeneralSecurityExceptions()).thenReturn(Collections.<GeneralSecurityException>emptyList());
        when(response.getAuthenticationMessages()).thenReturn(Collections.<Message>emptyList());

        return response;
    }

    protected final Authentication getConstructedAuthentication() {
        final AuthenticationRequest authenticationRequest = mock(AuthenticationRequest.class);
        when(authenticationRequest.getAuthenticationRequestDate()).thenReturn(new Date());
        when(authenticationRequest.getCredentials()).thenReturn(Collections.<Credential>emptyList());
        when(authenticationRequest.isLongTermAuthenticationRequest()).thenReturn(false);
        return this.authenticationFactory.getAuthentication(Collections.<String, List<Object>>emptyMap(), authenticationRequest, "myMethod");
    }

    @Before
    public void setUp() throws Exception {
        this.sessionStorage = getSessionStorage();
    }

    @Test
    public final void testCreateSession() {
        final Session session = this.sessionStorage.createSession(getAuthenticationResponse("test"));
        assertNotNull(session);
    }

    @Test
    public final void testDestroySessionThatExists() {
        final Session session = this.sessionStorage.createSession(getAuthenticationResponse("test"));
        assertNotNull(session);

        assertNotNull(this.sessionStorage.findSessionBySessionId(session.getId()));

        assertNotNull(this.sessionStorage.destroySession(session.getId()));
        assertNull(this.sessionStorage.destroySession(session.getId()));
    }

    @Test
    public final void testDestroySessionThatDoesNotExist() {
        assertNull(this.sessionStorage.destroySession("sessionThatDoesNotExist"));
    }

    @Test
    public final void testRetrieveRootSessionThatExists() {
        final Session session = this.sessionStorage.createSession(getAuthenticationResponse("test"));
        assertNotNull(session);

        final Session session2 = this.sessionStorage.findSessionBySessionId(session.getId());
        assertNotNull(session2);
        
        assertEquals(session, session2);
    }

    @Test
    public final void testRetrieveRootSessionThatDoesNotExist() {
        final Session session = this.sessionStorage.createSession(getAuthenticationResponse("test"));
        assertNotNull(session);

        final Session session2 = this.sessionStorage.findSessionBySessionId("FOOBAR");
        assertNull(session2);
    }

    @Test
    public final void testRetrieveSessionsForUserThatDoesNotExist() {
        this.sessionStorage.createSession(getAuthenticationResponse("test"));
        this.sessionStorage.createSession(getAuthenticationResponse("test"));

        final Set<Session> sessions = this.sessionStorage.findSessionsByPrincipal("FOOBAR");

        assertTrue(sessions.isEmpty());
    }

    @Test
    public final void testRetrieveSessionsForUserThatDoestExist() {
        this.sessionStorage.createSession(getAuthenticationResponse("test"));
        this.sessionStorage.createSession(getAuthenticationResponse("test"));
        this.sessionStorage.createSession(getAuthenticationResponse("FOOBAR"));

        assertEquals(2, this.sessionStorage.findSessionsByPrincipal("test").size());
        assertEquals(1, this.sessionStorage.findSessionsByPrincipal("FOOBAR").size());
    }

    // TODO retrieve by access, as well as delegated sessions

    @Test
    public final void testPurge() {
        getSessionStorage().purge();
    }

    @Test
    public final void testCleanable() {
        final SessionStorage sessionStorage = getSessionStorage();

        if (sessionStorage instanceof Cleanable) {
            // TODO do stuff
        }
    }
}
