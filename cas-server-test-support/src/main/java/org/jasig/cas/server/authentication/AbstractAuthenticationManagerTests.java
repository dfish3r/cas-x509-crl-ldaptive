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

package org.jasig.cas.server.authentication;

import org.jasig.cas.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Abstract tests for {@link org.jasig.cas.server.authentication.AuthenticationManager}
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractAuthenticationManagerTests {

    private AuthenticationManager authenticationManager;

    /**
     * Returns the {@link org.jasig.cas.server.authentication.AuthenticationManager} to use
     * for the tests.
     *
     * @return the {@link org.jasig.cas.server.authentication.AuthenticationManager}.  Cannot be null.
     */
    protected abstract AuthenticationManager getAuthenticationManager();

    @Before
    public final void setUp() throws Exception {
        this.authenticationManager = getAuthenticationManager();
    }


    @Test
    public final void successfulAuthentication() throws Exception {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithSameUsernameAndPassword();
        final AuthenticationRequest authenticationRequest = TestUtils.getAuthenticationRequest(c);
        final AuthenticationResponse authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        assertNotNull(authenticationResponse);
        assertTrue(authenticationResponse.succeeded());
        assertEquals(c.getUserName(), authenticationResponse.getPrincipal().getName());
    }

    @Test
    public final void failedAuthentication() throws Exception {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword();
        final AuthenticationRequest authenticationRequest = TestUtils.getAuthenticationRequest(c);
        final AuthenticationResponse authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        assertNotNull(authenticationResponse);
        assertFalse(authenticationResponse.succeeded());
    }

    @Test
    public final void noHandlerFound() {
        final Credential c = new Credential() {
            // nothing to do here.
        };

        final AuthenticationRequest authenticationRequest = TestUtils.getAuthenticationRequest(c);
        final AuthenticationResponse authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        assertNotNull(authenticationResponse);
        assertFalse(authenticationResponse.succeeded());
    }

    @Test
    public final void noResolverFound() {
        final UrlCredential c = new UrlCredential() {
            public URL getUrl() {
                return null;
            }
        };

        final AuthenticationRequest authenticationRequest = TestUtils.getAuthenticationRequest(c);
        final AuthenticationResponse authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        assertNotNull(authenticationResponse);
        assertFalse(authenticationResponse.succeeded());
    }

    protected final AuthenticationHandler getUsernamePasswordAuthenticationHandler() {
        return new AuthenticationHandler() {
            public boolean supports(final Credential credentials) {
                return credentials instanceof UserNamePasswordCredential;
            }

            public void authenticate(final Credential credentials) throws GeneralSecurityException {
                final UserNamePasswordCredential c = (UserNamePasswordCredential) credentials;
                if (!c.getUserName().equals(c.getPassword())) {
                    throw new GeneralSecurityException();
                }
            }

            public String getName() {
                return null;
            }
        };
    }

    protected final AuthenticationHandler getUrlCredentialAuthenticationHandler() {
         return new AuthenticationHandler() {
            public boolean supports(final Credential credentials) {
                return credentials instanceof UrlCredential;
            }

            public void authenticate(final Credential credentials) throws GeneralSecurityException {

            }

            public String getName() {
                return null;
            }
        };
    }

    protected final CredentialToPrincipalResolver getUsernamePasswordCredentialsToPrincipalResolver() {
        return new CredentialToPrincipalResolver() {
            public AttributePrincipal resolve(final Credential credentials) {
                final UserNamePasswordCredential c = (UserNamePasswordCredential) credentials;
                final AttributePrincipal p = mock(AttributePrincipal.class);

                when(p.getName()).thenReturn(c.getUserName());

                return p;
            }

            public boolean supports(final Credential credentials) {
                return credentials instanceof UserNamePasswordCredential;
            }
        };
    }

    protected final AuthenticationFactory getAuthenticationFactory() {
        return new AuthenticationFactory() {
            public Authentication getAuthentication(final Map<String, List<Object>> authenticationMetaData, final AuthenticationRequest authenticationRequest, final String authenticationType) {
                return mock(Authentication.class);
            }
        };
    }
}
